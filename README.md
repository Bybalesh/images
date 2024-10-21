---
tags:
  - LANG/RU
  - STRUCT/SYSTEM
---
---
![logo | center | 50%](./images/logo.png)
![Граф компетенций | center | 100%](./images/GrafCompetition.gif)

# Термины и определения:
1. **Граф компетенций** (**ГК**)- структура содержащая узлы с различными [[Описание хештегов#Структурные роли узлов|ролями]]
2. **Матрица компетенций** (**МК**) в **широком** смысле это - это HR-инструмент, который помогает оценить ключевые способности сотрудника для конкретной должности или области (**[Иточник](https://b2b.productstar.ru/blog/obuchenie_pod_matrizu)**). Чаще всего представляется в виде таблицы, но не ограничивается ею;
3. **Матрица компетенций** (**МК**) в **узком** смысле это - структура генерируемая на основе **графа компетенций** для конкретной роли в конкретной команде;
4. [[Описание хештегов#Структурные роли узлов#Список|Системный, информационный, тематический узел]]; 
---
# Цели графа компетенций:
1. Визуализировать знания в приятном формате для чтения, добавления и перемещения;
2. Автоматизировать и унифицировать оценку знаний программистов;
	- Структура матрицы компетенции генерируется автоматически, исходя из наполнения [уровней компетенций](Описание%20хештегов.md#Уровень%20компетенций), выбранных [[Описание хештегов#Структурные роли узлов#Список|тематических узлов]] и их пороговых значений.
3. Масштабировать и адаптировать оценку под цели команд;
	- Каждая команда будет иметь возможность генерировать структуру матрицы компетенций и на её основе получать материал для тестирования.   
4. Создать открытое сообщество систематизирующее знания и их оценку.
	- Предоставить возможность каждому желающему поучаствовать в создании базы знаний и оценок;
	- Валидировать структуру нововведений через прогон тестов в CI;
	- Валидировать смысловую составляющую через менторов и владельцев графа.
p.s. в цели графа компетенций не входит мониторинг знаний штатных сотрудников.
---
# Быстрый старт
## Шаг №1. Генерация МК
[[App/CompetenceMatrixGenerator/Описание работы Competence Matric Generator (CMG)|Описание работы Competence Matric Generator (CMG)]]
## Шаг №2. Генерация тестовых заданий
#TODO Сделать описание в проекте App/EstimatingTaskGenerator и добавить сюда
**Генерирует** и **проверяет** задания
## Шаг №3. Генерация маршрута обучения
#TODO Сделать описание в проекте App/RoadMapGenerator и добавить сюда
# Предисловие
## Для HR
Если ты не знаешь, как скачать и установить GIGA IDE, то попроси технарей сделать для тебя визуальный интерфейс. 
## Для интервьюера
Всё начинается с того, что в команду понадобился новый сотрудник и команде следует оценить его знания. Раз ты это читаешь, то тебе выпала честь провести собеседование или хотя бы подготовить МК и вопросы к ней.
Этот проект тебе поможет:
1. Сгенерировать МК под твои нужды;
2. Сгенерировать тесты и оценить их;
3. Дать обратную связь соискателю.
Ну или взять уже готовую)
## Для обучающегося
Поздравляю! Это одна из лучших баз знаний, которые ты мог найти.
Проект поможет тебе подготовиться к интересующей тебя вакансии и кто знает, может на собеседовании тебе будут задавать эти вопросы)

---
# Быстрый старт для контрибьюторов
Проект содержит две ветки:
1. **MAIN** - содержит все структурные узлы;
2. **information** - содержит только информационные и тематические узлы;
Контрибьютор может помочь проекту несколькими способами:
 1. Добавить в информацию в любой раздел [[#Дорожная карта развития|дорожной карты развития]];
 2. Добавить в структуру тематические узлы из раздела [[#Перечень недостающих тем|перечень недостающих тем]];
 3. Добавить в структуру оценивающих узлов;
 4. Предложить новую функцию и её реализацию.
Чтобы понять где твоя помощь нужна больше, воспользуйся [[docs/system/Obsidian DataView статистика|Obsidian DataView статистика]] или генерируемой статистикой приложения StatisticGenerator.
**Подсказки**:
Один PullRequset должен состоять из двух коммитов:
1. 1-й содержит тематические узлы и информационные узлы;
2. 2-1 содержит оценивающие узлы.
Это позволит использовать cherry pick для переноса коммита из **MAIN** ветки в **information** ветку, которая содержит только теорию и топики.
![Разница в ветках|center|800](./images/mastervsestimationbranch.png)
Устанавливаем Obsidian и плагины:
1. Dataview;
3. Editing toolbar;
4. Iconize;
Установить настройки:
1. Options -> Files and links -> Wikilinks -> on
---
# Дорожная карта развития

## Задания

### Генерация МК ([[App/CompetenceMatrixGenerator/Описание работы Competence Matric Generator (CMG)|CMG]])
- [x] Создать Kotlin модуль App/CompetenceMatrixGenerator
- [ ] Реализовать функционал по генерации описанный в [[App/CompetenceMatrixGenerator/Описание работы Competence Matric Generator (CMG)|CMG]]
### Генерация тестовых заданий
- [ ] Создать Kotlin модуль App/EstimatingTaskGenerator (ETG) по аналогии с CMG
- [ ] Создать описание для ETG
### Генерация маршрута обучения
- [ ] Создать Kotlin модуль App/RoadMapGenerator (RMG) по аналогии с CMG
- [ ] Создать описание для RMG
### Генерация статистики
- [ ] Создать Kotlin модуль App/StatisticGenerator (SG) по аналогии с CMG
- [ ] Создать описание для SG (он нужен для генерации статистики по аналогии с [[docs/system/Obsidian DataView статистика|Obsidian DataView статистика]], но для пользователей у которых нет Obsidian)
- [ ] Модуль должен пробегать по всем тестам и заполнять документ: [[docs/system/Описание тестирования консистнтности|Описание тестирования консистнтности]]
### Общие
- [ ] Сделать так, чтобы проходил тест checkCommonRuleWhatAllTagsYamlFrontMatterHaveDescriptionTest
- [ ] Написать тест checkCommonRuleWhatAllTagsFromMdHaveDescriptionTest 
- [ ] Проверять целостность ссылок во время CI
- [ ] Настроить запуск тестов во время CI при создании PR
- [ ] Настроить изменение кода только через PR с апрувами
- [ ] Разобрать файл [[Описание тестирования консистнтности]] и вынести пункты с подробным описанием в задания.
## Перечень недостающих тем(топиков)
1. #TODO сортировать по приоритету (придумать механизм голосования).
## Идеи для реализации:
1. Предлагаю сделать визуализацию результатов тестирования графа компетенций, как на рисунке ниже.
	![Как отображать скилы|center|300](./images/SkillsIdeaRepresentation.png)
2. Можно создать метрику актуальности информационного узла (темы/топик). Она будет собирать информацию о том какое кол-во раз топик использовался для генерации теста.
3. Создать рейтинговую таблицу по каждому топику.
4. Запускать в CI сборках [[Описание тестирования консистнтности|проверки на консистентность]]. Проверки на консистентность писать, используя PSI интерфейсы IDEA, чтобы иметь возможность перенести наработки из проверки в CI в проверки через плагин.   
---
# Вдохновлено:
1. Шаблон 1 матрицы - Александр Шелаухов; 
2. Шаблон 2 матрицы - Родин Денис;
3. Курс Java Middle Developer - Ленок Антон;
4. Курс Java Beginner - Кудряшёв Сергей;
5. "Куда  расти?" с Андреем Смирновым;
---

# ЧАВО (Частые вопросы)
- Как работать с Mark Down разметкой?
	- [[Шпаргалка по синтаксису разметки Markdown|Что такое Mark Down]];
	- Инструкция для Obsidian - [почитать](https://publish.obsidian.md/help-ru/Начните+здесь);
	- Инструкция для Obsidian - [посмотреть](https://rutube.ru/video/bec4ef4f5aea83cc161ad98569e414af/)
- Почему выбран Mark Down ?
	1. MD - хорошо человекочитаемый формат, который поддерживает множество приложений и парсеров.
	2. Проще, чем HTML.
	3. Его изменения легко просматриваются в GIT и многие сайты-репозитории поддерживают его отображение.
- Почему этот репозиторий не в CodeHub?
	1. Вся информация содержащаяся здесь не имеет конфиденциального характера.
	2. Открытость репозитория призвана привлекать внимание к работе в компании  и её продуктам (GitVerse). 
+ Тестируемые подготовят ответы по всей базе знаний вопросы и не будут учить материал !?
	+ Вопросы будут лежать в отдельной ветке "estimation" и она будет закрыта для обычных пользователей.
+ Как формируется пороговое значение для уровней компетенций?
	+ Пороговое значение до этапа формирования **МК** для [[Шаблон тематического узла| тематического узла]]может быть задано в ручном режиме в разделе [[Шаблон тематического узла#Пороговые значения|пороговые значения]]. 
	+ Если раздел не заполнен, то 100% делится на равные части таким количеством точек, сколько содердит [[Описание хештегов#Уровень компетенций#Список|список уровней компетенций]].
	+ После формирования **МК**, команда может изменить эти уровни в шаблоне на своё усмотрение.
+ Как происходит рачёт % знания по теме?
	+ Он рассчитывается по формуле:
	$$
	(100 * R)/S 
	$$
	 , где
	 R - кол-во набранных баллов по теме и подтемам;
	 S - максимально возможное кол-во баллов по теме и подтемам;
	 Оценивающие узлы имеют разную [[Описание хештегов#Сложность Описание хештегов Структурные роли узлов оценивающих узлов|сложность]]. 
	 Их ценность распределяется следующим образом:
	 #COMPLEXITY/ADVANCED= 2 * #COMPLEXITY/MEDIUM ;
	 #COMPLEXITY/MEDIUM= 2 * #COMPLEXITY/BASIC ;
	 #COMPLEXITY/BASIC =  максимум 5 баллов;
+ Почему некоторые [[Шаблон тематического узла#Пороговые значения*|заголовки]] в шаблонах написаны со звёздочкой в конце?
	+ Звёздочка означает необязательность наличие такого заголовка в узле.
---
