#!/bin/bash
set -e

# 1. 注册用户
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"chaoren111","password":"111","email":"chaoren111@example.com"}')
echo "注册响应: $REGISTER_RESPONSE"

# 2. 登录获取token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"chaoren111","password":"111"}')
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "登录token: $TOKEN"

# 3. 创建plan
PLAN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/plans \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"串联测试计划","description":"计划描述1","startDate":"2024-07-15","endDate":"2024-08-15"}')
PLAN_ID=$(echo $PLAN_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "创建计划ID: $PLAN_ID"

# 4. 创建task
TASK_RESPONSE=$(curl -s -X POST http://localhost:8080/api/plans/$PLAN_ID/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"串联测试任务","description":"任务描述1","dueDate":"2024-07-20","status":"TODO","priority":"MEDIUM","progress":0}')
TASK_ID=$(echo $TASK_RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo "创建任务ID: $TASK_ID"

# 5. 创建task进度
PROGRESS_RESPONSE=$(curl -s -X POST http://localhost:8080/api/tasks/$TASK_ID/progress \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"progress":10,"comment":"第一次进度"}')
echo "记录进度响应: $PROGRESS_RESPONSE"

# 6. get plan
echo "\n查询计划:"
curl -s -X GET http://localhost:8080/api/plans/$PLAN_ID \
  -H "Authorization: Bearer $TOKEN" | jq

# 7. get task
echo "\n查询任务:"
curl -s -X GET http://localhost:8080/api/plans/$PLAN_ID/tasks \
  -H "Authorization: Bearer $TOKEN" | jq

# 8. get task进度
echo "\n查询任务进度:"
curl -s -X GET http://localhost:8080/api/tasks/$TASK_ID/progress \
  -H "Authorization: Bearer $TOKEN" | jq

# 9. 修改task进度
echo "\n修改任务进度:"
curl -s -X POST http://localhost:8080/api/tasks/$TASK_ID/progress \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"progress":60,"comment":"第二次进度"}' | jq

# 10. 修改task描述
echo "\n修改任务描述:"
curl -s -X PUT http://localhost:8080/api/tasks/$TASK_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"串联测试任务","description":"任务描述2","dueDate":"2024-07-20","status":"TODO","priority":"MEDIUM","progress":60}' | jq

# 11. 修改plan描述
echo "\n修改计划描述:"
curl -s -X PUT http://localhost:8080/api/plans/$PLAN_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"串联测试计划","description":"计划描述2","startDate":"2024-07-15","endDate":"2024-08-15"}' | jq

# 12. get plan
echo "\n再次查询计划:"
curl -s -X GET http://localhost:8080/api/plans/$PLAN_ID \
  -H "Authorization: Bearer $TOKEN" | jq

# 13. get task
echo "\n再次查询任务:"
curl -s -X GET http://localhost:8080/api/plans/$PLAN_ID/tasks \
  -H "Authorization: Bearer $TOKEN" | jq

# 14. get task进度
echo "\n再次查询任务进度:"
curl -s -X GET http://localhost:8080/api/tasks/$TASK_ID/progress \
  -H "Authorization: Bearer $TOKEN" | jq

echo "\n全部API串联验证完成！" 