#!/bin/bash
# 用法: ./test_task_progress_get.sh <token> <taskId>

TOKEN=$1
TASK_ID=$2

if [ -z "$TOKEN" ] || [ -z "$TASK_ID" ]; then
  echo "用法: $0 <token> <taskId>"
  exit 1
fi

API_URL="http://localhost:8080/api/tasks/$TASK_ID/progress"

curl -X GET "$API_URL" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"

echo 