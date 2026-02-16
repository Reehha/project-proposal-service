# project-proposal-service (Spring Boot + DynamoDB + JWT)

## DynamoDB tables required
Create **two tables** in DynamoDB:

### 1) user
- Partition key: `uid` (String)

### 2) project
- Partition key: `pid` (String)

> NOTE: For the MVP this repo scans to find users by email. For production, add a GSI on `email` and query it.

## Run in STS (Spring Tool Suite)
1. File -> Import -> Maven -> Existing Maven Projects
2. Select this folder: `project-proposal-service`
3. Right click project -> Maven -> Update Project
4. Set Java to 21 (Project properties -> Java Compiler)
5. Add env var for JWT secret (min 32 chars):
   - `APP_JWT_SECRET=your-super-long-secret-key-32-chars-min`
6. Ensure your AWS credentials are available to the app:
   - Option A: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`
   - Option B: AWS profile (`~/.aws/credentials`)
7. Run: `ProjectProposalServiceApplication`

Server will start at: `http://localhost:8081`

## API quick test (curl)

### Register (no auth)
```bash
curl -i -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":"Ree",
    "lastName":"Ree",
    "dob":"1999-01-01",
    "email":"ree@example.com",
    "gender":"F",
    "phoneNumber":"+96800000000",
    "nationality":"OM",
    "address":"Muscat",
    "password":"Password@123",
    "role":"ADMIN"
  }'
```

### Login -> get token
```bash
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ree@example.com","password":"Password@123"}' | jq -r .token)
echo $TOKEN
```

### Create project (auth required)
```bash
curl -i -X POST http://localhost:8081/api/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pname":"New Proposal",
    "timelineJson":"[
      {\"month\":\"Mar\",\"year\":2026,\"desc\":\"Discovery & planning\"},
      {\"month\":\"Apr\",\"year\":2026,\"desc\":\"Design\"},
      {\"month\":\"May\",\"year\":2026,\"desc\":\"Development 1\"},
      {\"month\":\"Jun\",\"year\":2026,\"desc\":\"Development 2\"},
      {\"month\":\"Jul\",\"year\":2026,\"desc\":\"Testing\"},
      {\"month\":\"Aug\",\"year\":2026,\"desc\":\"Launch & handover\"}
    ]",
    "clientReq":[{"title":"Goal","desc":"Build a proposal generator"}],
    "services":[{"title":"Delivery","desc":"Weekly updates"}],
    "pricingPlans":[
      {"name":"Basic","price":"$199","recommended":false,"features":["Feature A","Feature B"]},
      {"name":"Pro","price":"$499","recommended":true,"features":["Everything in Basic","Priority support"]}
    ]
  }'
```

### Get project (auth)
```bash
curl -i http://localhost:8081/api/projects/<PID> -H "Authorization: Bearer $TOKEN"
```
