# WorkWithDocument — сервис проверки документов

## Описание проекта
**WorkWithDocument** — это серверное приложение на **Spring Boot**, предназначенное для **анализа, проверки и валидации офисных документов (DOCX)** на соответствие заданным правилам форматирования и структуры.  
Приложение предоставляет REST API для загрузки документа, выполнения проверки и получения отчёта о найденных несоответствиях.

---

## Основные возможности
- Загрузка и обработка документов (DOCX)  
- Валидация по набору правил (например, отступы, шрифты, поля, структура)  
- Гибкая конфигурация правил через YAML-файл (`rules.yaml`)  
- Формирование отчёта о найденных нарушениях с указанием уровня серьёзности  
- Расширяемый движок правил (`RuleEngine`)  
- Простая интеграция с другими системами через REST API

---

## Технологии
- **Java 17+**  
- **Spring Boot 3+**  
- **Spring Web**  
- **Apache POI** (для работы с документами DOCX)  
- **YAML Configuration**  
- **Maven**

---

## Структура проекта
```
src/
 └── main/
     ├── java/com/dmitry/document/
     │   ├── DocumentApplication.java       # Точка входа в приложение
     │   ├── api/ValidationController.java  # REST API для проверки документов
     │   ├── service/ValidationService.java # Основная бизнес-логика
     │   ├── model/                         # Модели документа и утилиты
     │   │   ├── DocxLoader.java
     │   │   ├── DocumentModel.java
     │   │   └── Location.java
     │   ├── engine/                        # Движок и обработка правил
     │   │   ├── RuleEngine.java
     │   │   └── RulesConfig.java
     │   ├── rules/                         # Реализация конкретных правил
     │   │   ├── MarginsRule.java
     │   │   ├── TypographyRule.java
     │   │   ├── Rule.java
     │   │   └── ValidationIssue.java
     │   └── config/                        # Конфигурация приложения
     │       ├── AppConfig.java
     │       └── RulesProperties.java
     └── resources/
         ├── application.yml (или .properties)
         └── rules.yaml                     # Конфигурация правил проверки
```

---

## Как запустить проект

### Клонирование репозитория
```bash
git clone https://github.com/<твой_профиль>/WorkWithDocument.git
cd WorkWithDocument
```

### Сборка и запуск
#### Через Maven Wrapper:
```bash
./mvnw spring-boot:run
```

#### Или вручную:
```bash
mvn clean package
java -jar target/WorkWithDocument-0.0.1-SNAPSHOT.jar
```

Приложение запустится по адресу:
```
http://localhost:8080
```

---

## Примеры API

### Проверка документа
**POST** `/api/validate`  
Загрузка DOCX-файла для анализа.

Пример запроса (multipart/form-data):
```
file: document.docx
```

Пример ответа:
```json
{
  "issues": [
    {
      "rule": "TypographyRule",
      "severity": "WARNING",
      "message": "Использован неверный шрифт (ожидается Times New Roman, найден Arial)"
    },
    {
      "rule": "MarginsRule",
      "severity": "ERROR",
      "message": "Левые поля меньше 2 см"
    }
  ]
}
```

---

## Конфигурация правил
Файл `rules.yaml` содержит описание правил проверки документов.  
Пример:
```yaml
rules:
  typography:
    font: "Times New Roman"
    size: 14
  margins:
    left: 2.0
    right: 2.0
    top: 2.0
    bottom: 2.0
```

---

## Архитектура
1. Клиент отправляет документ через API.  
2. `DocxLoader` обрабатывает документ и передаёт в `RuleEngine`.  
3. `RuleEngine` применяет правила из `rules.yaml`.  
4. `ValidationService` формирует результат проверки.  
5. Ответ возвращается в виде JSON-отчёта.  
