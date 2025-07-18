#!/bin/bash

echo "测试用户注册API..."

# 测试用户注册
echo "发送注册请求..."
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"

echo ""
echo "测试重复注册（应该失败）..."
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"

echo ""
echo "测试注册完成" 