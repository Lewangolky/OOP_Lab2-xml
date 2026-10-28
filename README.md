# Лабораторна робота 2 — XML / XSD / XSL (Холодна зброя)

## Структура

| Файл | Призначення |
|------|-------------|
| `src/main/resources/knives.xml` | XML-документ (8 одиниць холодної зброї) |
| `src/main/resources/knives.xsd` | Схема: прості й комплексні типи, переліки, граничні значення, `xs:ID`, обов'язкові атрибути, `xs:choice` |
| `src/main/resources/knives-grouped.xsl` | XSLT: розбиває список на підсписки за типом (`dagger`, `sword`, ...) |
| `com.oop.knives.model` | Java-класи, що відповідають схемі |
| `com.oop.knives.parser` | Парсери DOM, SAX, StAX + спільний `KnifeAssembler` (без дублювання коду) |
| `com.oop.knives.sort` | `KnifeSort` — компаратори (`Comparator<Knife>`) |
| `com.oop.knives.validation` | Валідація XML за XSD |
| `com.oop.knives.transform` | Застосування XSL-перетворення |
| `com.oop.knives.KnifeApplication` | Точка входу |

## Запуск

```bash
mvn verify
```

Тести + звіт покриття JaCoCo: `target/site/jacoco/index.html`.

```bash
mvn compile exec:java
```

Валідує документ, розбирає його трьома парсерами, виводить відсортовані списки в лог
(консоль і `logs/knives.log`) та записує результат XSL-перетворення в `target/knives-grouped.xml`.
