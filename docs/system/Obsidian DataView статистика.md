---
tags:
  - LANG/RU
  - STRUCT/SYSTEM
---
---
# Топ 15 непокрытых тем вопросами 
```dataview
TABLE length(filter(flat(file.outlinks.tags), (t) => contains(t,"ESTIMATING"))) as "Кол-во ?"
FROM "docs/topic" and #STRUCT/TOPIC 
WHERE contains(file.outlinks.tags, "ESTIMATING")
SORT length(filter(flat(file.outlinks.tags), (t) => contains(t,"ESTIMATING"))) ASC
LIMIT 15
```