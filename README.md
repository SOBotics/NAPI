# NAPI
APIs for Natty

Available routes:

- `api/reports/all` : Retrieves all the Stack Overflow Natty reports which are pending feedback. 
- `api/reports/all/au` : Retrieves all the Ask Ubuntu Natty reports which are pending feedback. 
- `api/stored/all` : Retrieves all the Stack Overflow Natty reports which have been provided feedback
- `api/stored/all/au` : Retrieves all the Ask Ubuntu Natty reports which have been provided feedback
- `api/stored/{ids}`: Retrieves all the Stack Overflow Natty reports given some comma-separated ids
- `api/stored/{ids}/au`: Retrieves all the Ask Ubuntu Natty reports given some comma-separated ids
- `api/list/blacklistedWords` : Retrieves all the words which are blacklisted on Natty. 
- `api/list/whitelistedWords` : Retrieves all the words which are whitelisted on Natty. 
