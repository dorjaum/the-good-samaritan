# Backend Rules

Use package-by-feature.

Each module contains:

- controller
- service
- repository
- entity
- dto

Rules:

- constructor injection only
- no field injection
- UUID ids
- DTO for requests/responses
- no entity exposure
- transactions at service layer