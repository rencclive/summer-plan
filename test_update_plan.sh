#!/bin/bash

# 测试更新计划API

if [ -z "$1" ]; then
  echo "用法: $0 <计划ID>"
  exit 1
fi
PLAN_ID=$1

# 登录获取token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
if [ -z "$TOKEN" ]; then
    echo "登录失败，无法获取token"
    echo "登录响应: $LOGIN_RESPONSE"
    exit 1
fi

echo "获取到token: $TOKEN"

echo "发送更新计划请求..."
curl -X PUT http://localhost:8080/api/plans/$PLAN_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "更新后的测试计划",
    "description": "更新后的描述",
    "startDate": "2024-07-10",
    "endDate": "2024-09-01",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "progress": 50,
    "tags": "更新,测试"
  }' \
  -w "\nHTTP状态码: %{http_code}\n" 