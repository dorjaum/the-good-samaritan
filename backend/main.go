package main

import (
	"net/http"
	"sync"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

// Person is the domain model; ID is assigned by the server on create.
type Person struct {
	ID   uuid.UUID `json:"id"`
	Name string    `json:"name"`
}

type createPersonRequest struct {
	Name string `json:"name"`
}

type personStore struct {
	mu   sync.RWMutex
	list []Person
}

func (s *personStore) add(p Person) {
	s.mu.Lock()
	defer s.mu.Unlock()
	s.list = append(s.list, p)
}

func (s *personStore) all() []Person {
	s.mu.RLock()
	defer s.mu.RUnlock()
	out := make([]Person, len(s.list))
	copy(out, s.list)
	return out
}

func main() {
	r := gin.Default()

	store := &personStore{}

	r.GET("/health", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{"status": "ok"})
	})

	r.GET("/person", func(c *gin.Context) {
		c.JSON(http.StatusOK, store.all())
	})

	r.POST("/person", func(c *gin.Context) {
		var req createPersonRequest
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid JSON body"})
			return
		}
		if req.Name == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "name is required"})
			return
		}
		p := Person{
			ID:   uuid.New(),
			Name: req.Name,
		}
		store.add(p)
		c.JSON(http.StatusCreated, p)
	})

	if err := r.Run(":8080"); err != nil {
		panic(err)
	}
}
