# Domain Model

## Person

Represents any platform user.

Fields:
- id UUID
- name
- email
- role

Roles:
- beneficiary
- volunteer
- professional

---

## Need

Represents a help request.

Fields:
- id UUID
- person_id
- title
- description
- category
- urgency_level
- status