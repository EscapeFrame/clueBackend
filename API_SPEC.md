#### **3. API Endpoints**
**3.2. flow 단계**
사용자에게 받은 기획을 바탕으로 AI가 목차를 생성
- **Endpoint:** `POST /ai/v1/agents/flow`
- **Request Body**
  ```json
  {
    "studying_name": "string",
    "learning_purpose": "string",
    "main_words": ["string"],
    "links": ["string"]
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "words": [
        {
          "priority": 1,
          "index": "string",
          "iconNumber": 2
        }
      ]
    },
    "message": "Flow successfully generated"
  }
  ```

**3.4. flow 피드백 단계**
AI가 만들어준 목차가 마음에 안들어서 자신이 추가한 데이터와 함께 다시 AI에게 전송
- **Endpoint:** `PATCH /ai/v1/agents/{agent_id}/flow`
- **Request Body:**
  ```json
  {
    "studying_name": "string",
    "learning_purpose": "string",
    "main_words": ["string"],
    "links": ["string"],
    "words": [
      {
        "priority": 1,
        "index": "string",
        "iconNumber": 2
      }
    ]
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "words": [
        {
          "priority": 1,
          "index": "string",
          "iconNumber": 2
        }
      ]
    },
    "message": "Flow feedback applied"
  }
  ```

**3.3. doc 단계**
- **Endpoint:** `POST /ai/v1/agents/doc`
- **Request**
  ```json
  {
    "studying_name": "string",
    "learning_purpose": "string",
    "main_words": ["string"],
    "links": ["string"],
    "words": [
      {
        "priority": 1,
        "index": "string",
        "iconNumber": 2
      }
    ]
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "docs": [
        {
          "index": "string",
          "content": "string"
        }
      ]
    },
    "message": "Documentation created successfully"
  }
  ```

**3.4. doc 피드백 단계**
- **Endpoint:** `PATCH /ai/v1/agents/doc/feedback`
- **Request Body**
  ```json
  {
    "studying_name": "string",
    "learning_purpose": "string",
    "main_words": ["string"],
    "links": ["string"],
    "words": [
      {
        "priority": 1,
        "index": "string",
        "iconNumber": 2
      }
    ],
    "docs": [
      {
        "index": "string",
        "content": "string"
      }
    ]
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "docs": [
        {
          "index": "string",
          "content": "string"
        }
      ]
    },
    "message": "Documentation feedback applied"
  }
  ```

**3.5. graph 단계**
- **Endpoint:** `POST /ai/v1/agents/graph`
- **Request Body**
  ```json
  {
    "docs": [
      {
        "index": "string",
        "content": "string"
      }
    ]
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "nodes": [
        {
          "id": 1,
          "keyword": "string",
          "links": [2, 3]
        }
      ]
    },
    "message": "Graph generated successfully"
  }
  ```

**3.6. 제작 단계**
- **Endpoint:** `/api/v1/agents/{agentId}/complete`
- **Request Body**
  ```json
  {
    "data": {
      "content": "string"
    },
    "message": "Lesson markdown generated successfully"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "data": {
      "content": "string"
    },
    "message": "Lesson markdown generated successfully"
  }
  ```


---
### 🚀 CLUE Agent API 명세서 (v2 - 서버 상태 관리)

10년 차 개발자 관점에서 제안하는 'Agent 리소스' 기반의 RESTful API 명세서입니다. 서버가 각 단계의 상태를 `agent_id`에 저장하여 클라이언트의 부담을 줄이는 구조입니다.

---

### 1. Agent 생성 (Planning 단계)

`planning` 단계의 데이터를 받아 **새로운 Agent 리소스**를 생성하고 `agent_id`를 반환합니다.

* **Endpoint:** `POST /api/v1/agents`
* **Description:** 새로운 수업 자료 생성 Agent를 초기화합니다.
* **Request Body:**
    ```json
    {
      "studying_name": "string",
      "learning_purpose": "string",
      "main_words": ["string"],
      "links": ["string"]
    }
    ```
* **Response (201 Created):**
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "PLANNING_COMPLETED",
        "created_at": "2025-11-12T18:00:00Z"
      },
      "message": "Agent created successfully"
    }
    ```

---

### 2. Flow 생성 (Flow 단계)

`agent_id`를 기반으로 서버에 저장된 `planning` 데이터를 조회하여 `flow`(목차)를 생성합니다.

* **Endpoint:** `POST /api/v1/agents/{agent_id}/flow`
* **Description:** 해당 Agent의 목차(flow)를 생성합니다.
* **Request Body:** (Empty)
* **Response (200 OK):** (서버는 이 `flow` 결과를 DB에 저장합니다)
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "FLOW_GENERATED",
        "flow": {
          "words": [
            { "priority": 1, "index": "가상 DOM의 이해", "iconNumber": 2 }
          ]
        }
      },
      "message": "Flow successfully generated"
    }
    ```

---

### 3. Flow 피드백 (Flow 피드백 단계)

생성된 `flow`의 내용만 수정합니다.

* **Endpoint:** `PATCH /api/v1/agents/{agent_id}/flow`
* **Description:** 사용자가 수정한 목차(flow)를 업데이트합니다.
* **Request Body:**
    ```json
    {
      "words": [
        { "priority": 1, "index": "가상 DOM의 개념", "iconNumber": 2 },
        { "priority": 2, "index": "React와 VDOM", "iconNumber": 3 }
      ]
    }
    ```
* **Response (200 OK):**
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "FLOW_UPDATED",
        "flow": {
          "words": [ ... ] // 수정된 결과
        }
      },
      "message": "Flow feedback applied"
    }
    ```

---

### 4. Doc 생성 (Doc 단계)

`agent_id`를 기반으로 서버에 저장된 `flow` 데이터를 조회하여 `doc`(본문)을 생성합니다.

* **Endpoint:** `POST /api/v1/agents/{agent_id}/doc`
* **Description:** 해당 Agent의 목차를 기반으로 상세 본문(doc)을 생성합니다.
* **Request Body:** (Empty)
* **Response (200 OK):** (서버는 `doc` 결과를 DB에 저장합니다)
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "DOC_GENERATED",
        "doc": {
          "docs": [
            { "index": "가상 DOM의 개념", "content": "가상 DOM(Virtual DOM)은..." }
          ]
        }
      },
      "message": "Documentation created successfully"
    }
    ```

---

### 5. Doc 피드백 (Doc 피드백 단계)

생성된 `doc`의 내용만 수정합니다.

* **Endpoint:** `PATCH /api/v1/agents/{agent_id}/doc`
* **Description:** 사용자가 수정한 본문(doc)을 업데이트합니다.
* **Request Body:**
    ```json
    {
      "docs": [
        { "index": "가상 DOM의 개념", "content": "수정된 내용입니다. 가상 DOM은 UI의..." }
      ]
    }
    ```
* **Response (200 OK):**
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "DOC_UPDATED",
        "doc": { ... } // 수정된 doc
      },
      "message": "Documentation feedback applied"
    }
    ```

---

### 6. Graph 생성 (Graph 단계)

`agent_id`를 기반으로 서버에 저장된 `doc` 데이터를 조회하여 `graph`(키워드 연결)를 생성합니다.

* **Endpoint:** `POST /api/v1/agents/{agent_id}/graph`
* **Description:** 본문 내용을 기반으로 키워드 그래프를 생성합니다.
* **Request Body:** (Empty)
* **Response (200 OK):** (서버는 `graph` 결과를 DB에 저장합니다)
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "GRAPH_GENERATED",
        "graph": {
          "nodes": [
            { "id": 1, "keyword": "Virtual DOM", "links": [2, 3] },
            { "id": 2, "keyword": "React", "links": [1] },
            { "id": 3, "keyword": "DOM", "links": [1] }
          ]
        }
      },
      "message": "Graph generated successfully"
    }
    ```

---

### 7. Agent 전체 상태 조회

작업 중단 후 다시 이어하기 위해 Agent의 현재까지 모든 데이터를 조회합니다.

* **Endpoint:** `GET /api/v1/agents/{agent_id}`
* **Description:** 특정 Agent의 현재까지 작업 상태와 모든 데이터를 조회합니다.
* **Response (200 OK):**
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "GRAPH_GENERATED",
        "planning": {
          "studying_name": "React 기초",
          ...
        },
        "flow": {
          "words": [ ... ]
        },
        "doc": {
          "docs": [ ... ]
        },
        "graph": {
          "nodes": [ ... ]
        }
      }
    }
    ```

---

### 8. 제작 완료 (Complete 단계)

모든 단계를 종합하여 최종 마크다운 결과물을 생성합니다.

* **Endpoint:** `POST /api/v1/agents/{agent_id}/complete`
* **Description:** Agent의 모든 데이터를 종합하여 최종 수업 자료(Markdown)를 생성합니다.
* **Request Body:** (Empty. 또는 `graph` 피드백이 있다면 여기에 포함)
* **Response (200 OK):**
    ```json
    {
      "data": {
        "agent_id": "agent_uuid_abc123",
        "status": "COMPLETED",
        "final_markdown": "# React 기초\n\n## 1. 가상 DOM의 개념\n수정된 내용입니다. ... [관련 자료: DOM](...)"
      },
      "message": "Lesson markdown generated successfully"
    }
    ```