#!/bin/bash
# 登录获取token
token=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"chaoren111","password":"111"}' | \
  grep -o '"token":"[^"]*"' | cut -d '"' -f4)

echo "Token: $token"

echo "\n请求 /api/tasks/all 接口："
curl -s -X GET http://localhost:8080/api/tasks/all \
  -H "Authorization: Bearer $token" \
  -H 'Content-Type: application/json' | jq 