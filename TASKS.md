# Infinity Folder Launcher — декомпозиция работ

Цель: довести лаунчер до новой версии в RuStore по [Figma](https://www.figma.com/design/hpgrupgalhhqBGUlCttl6m/Android-Launcher--%2522Infinity-Folder-Launcher%2522-) и закрыть дыры текущего кода + отзывы.

**Проект:** `/Users/maksmolch/AndroidStudioProjects/InfinityFolderLauncher`  
**RuStore:** [Лаунчер с Бесконечными папками](https://www.rustore.ru/catalog/app/maks.molch.dmitr.makslauncher) (console app `2063541835`)  
**Текущая версия в сторе:** `1.0` (11 янв 2024), ~1k скачиваний, рейтинг ~4.7

Каждая задача рассчитана на **1–3 промпта** агенту. Идём сверху вниз; чекбокс — когда готово.

---

## Контекст: что уже есть в коде

Стек: Kotlin + Jetpack Compose, Home-intent в манифесте, хранение папок в `SharedPreferences` + Gson.

| Есть | Статус |
|------|--------|
| Splash → Onboarding → Main | работает |
| Сетка объектов в папке, long-press edit mode | частично |
| Добавить папку (имя + иконка) | есть |
| Добавить приложение (поиск) | есть |
| Перенос объектов в другую папку | есть |
| Удаление объектов из папки | есть |
| Вложенные папки («бесконечные») | идея есть (новая Activity с `FOLDER_NAME`) |
| Settings | **заглушка** (toast) |
| Add Widget | **TODO()** |
| Настройки «кол-во в строке» из описания RuStore | **нет в коде** (хардкод `4`) |
| Rename папки | **нет** |
| Reorder (drag) | **нет** |

**Важно для релиза:** в RuStore `applicationId` = `maks.molch.dmitr.makslauncher`, в локальном `build.gradle.kts` = `maks.molch.dmitr.infinityfolderlauncher`. Для обновления существующего приложения id должен совпасть со стором.

### Отзыв из RuStore (Николай, 25 янв 2024)

> Добавьте возможность изменения порядка папок и приложений, а также переименовывать папки.

Приоритет v1.1: **reorder + rename**.

### Экраны из Figma (ориентир UI)

- Main / Main Edit Mode (selection)
- Add/Edit Folder (имя + выбор иконки)
- Add Application (мультивыбор + поиск)
- Settings
- Add Widget / Website Widget (URL + иконка)
- Onboarding, превью папок с сеткой иконок внутри

---

## Фаза 0 — окружение (без полной Android Studio)

- [x] **0.1** SDK уже был: `~/Library/Android/sdk` (platform-tools, `platforms;android-36`, build-tools 35/36). Сборка без Studio: `./scripts/build-debug.sh` или `./gradlew :app:assembleDebug` + JDK 21. `BUILD SUCCESSFUL`, APK `app/build/outputs/apk/debug/app-debug.apk` (~11 MB). Телефон не был в `adb devices` — install позже.
- [x] **0.2** Добавлен `.cursorignore` (`.gradle/`, `app/build/`, `.idea/`, …). Root агента в multi-repo не двигали (ошибка Cursor) — работаем по абсолютному пути к проекту.
- [x] **0.3** Зафиксированы id и версии под обновление существующего приложения в RuStore.

> Решение по id: **обновление RuStore** → `applicationId = maks.molch.dmitr.makslauncher` · старт релиза: **versionName `1.1.0` / versionCode `2`**

---

## Фаза 1 — фундамент и стабильность (перед фичами)

- [x] **1.1** `ARCHITECTURE.md` — экраны, DAO, модель, навигация.
- [x] **1.2** Убраны `println` / debug toast’ы (NavBar, Main Settings stub, Onboarding, FolderSearch).
- [x] **1.3** Стек папок в Compose (`folderStack` id); одна Activity.
- [x] **1.4** `FolderDao.epoch: StateFlow` — UI перечитывает папку после мутаций.
- [x] **1.5** Стабильный `id` у `Folder` / `Application` (у apps = packageName); selection/move по id.
- [x] **1.6** Schema v1 в `FolderMeta` + миграция name-keyed → id-keyed prefs без потери данных.
- [x] **1.7** `launchMode=singleTask`, `stateNotNeeded`; back на корне глотается; back из Add* → Main. (Проверка на устройстве — при следующем `adb install`.)

---

## Фаза 2 — запросы из отзыва (must-have для v1.1)

- [ ] **2.1** Rename папки: UI (edit mode / long-press → Rename) + сохранение в DAO + обновление детей/ссылок.
- [ ] **2.2** Reorder внутри текущей папки: drag-and-drop в `LazyVerticalGrid` (или кнопки ↑↓ как MVP) + сохранение порядка в списке.
- [ ] **2.3** Persist order: поле `sortIndex` / порядок списка в JSON, тест «перезапуск — порядок тот же».
- [ ] **2.4** Ответ на отзыв в RuStore + скрин в «Что нового» (текст релиза).

---

## Фаза 3 — добить заявленный функционал (описание стора + NavBar)

- [ ] **3.1** Экран Settings: кол-во объектов в строке на Main и в поиске приложений (как в описании RuStore).
- [ ] **3.2** Подключить Settings к Main / AddApplication (убрать хардкод `objectNumberOnTheRow = 4`).
- [ ] **3.3** Действия над приложением: удалить с телефона (uninstall intent) vs убрать из папки — по Figma/описанию, без путаницы в копирайте.
- [ ] **3.4** Edit mode TopBar: реальный переход в Settings вместо toast.
- [ ] **3.5** NavBar: убрать debug toast’ы; корректный active state; не показывать только в edit, если по дизайну нужен и на Main — сверить Figma.

---

## Фаза 4 — виджеты (из onboarding + Figma)

- [ ] **4.1** Заглушку `Screen.AddWidget` заменить экраном-списком типов: системный виджет / website shortcut.
- [ ] **4.2** Website Widget: ввод URL + название + иконка → объект на сетке → открытие в браузере.
- [ ] **4.3** (Опционально v1.2+) `AppWidgetHost` — настоящие Android-виджеты; отдельно, тяжело.

---

## Фаза 5 — UI ближе к Figma

- [ ] **5.1** Тема/токены: цвета, скругления, типографика с макета (сейчас смесь Material purple + свои Base*).
- [ ] **5.2** Main: wallpaper (не логотип на весь экран), опционально часы/погода если есть на макете — или явно отложить.
- [ ] **5.3** Превью иконок внутри папки (мини-сетка 2×2 на иконке папки).
- [ ] **5.4** Add Folder / Add Application — визуальный паритет с Figma (поля, кнопки, состояния empty/error).
- [ ] **5.5** Onboarding: тексты/иллюстрации как в макете; Skip / Let's go без багов степпера.
- [ ] **5.6** Edit mode: чекбоксы/selection как на макете «Main Screen Edit Mode with selection».

---

## Фаза 6 — поиск и UX папок

- [ ] **6.1** Глобальный поиск по приложениям/папкам с главного экрана (если есть в Figma search bar).
- [ ] **6.2** Хлебные крошки / заголовок текущей папки + Home.
- [ ] **6.3** Запрет циклов при move (папку внутрь себя / потомка).
- [ ] **6.4** Пустая папка: понятный empty state + CTA «добавить».

---

## Фаза 7 — качество и релиз

- [ ] **7.1** Строки в `strings.xml` (RU по умолчанию); убрать хардкод английского в UI где нужно для RuStore.
- [ ] **7.2** Иконка/название приложения как в сторе («Лаунчер с Бесконечными папками» vs Infinity Folder Launcher).
- [ ] **7.3** `assembleRelease` + подпись (keystore); checklist установки как Home.
- [ ] **7.4** Ручной смоук: onboarding → создать папку → вложить папку → добавить 3 приложения → reorder → rename → move → clear → settings grid.
- [ ] **7.5** Залить в RuStore: `versionCode++`, текст «Что нового» (reorder, rename, settings, …).
- [ ] **7.6** Ответить Николаю в отзывах, что сделано.

---

## Фаза 8 — бэклог (не блокирует v1.1)

- [ ] Жесты (свайп на поиск / уведомления) — если появятся в Figma.
- [ ] Иконпаки / темы иконок.
- [ ] Бэкап/restore раскладки.
- [ ] Планшет / складные.
- [ ] Анимации открытия папки.
- [ ] Виджеты системы (`AppWidgetHost`).

---

## Рекомендуемый порядок первых сессий

1. **0.1–0.3** — собрать debug на телефоне  
2. **1.3–1.5** — навигация + id в модели  
3. **2.1–2.3** — rename + reorder (закрываем отзыв)  
4. **3.1–3.2** — settings grid  
5. **5.x** по кускам UI  
6. **4.2** website widget  
7. **7.x** релиз  

---

## Как пилить с агентом

В промпте писать номер задачи, например:

> Сделай задачу **2.1** из `TASKS.md`: rename папки. Не трогай reorder.

После мержа — отметить `- [x]` здесь же.

---

## Ссылки

- Figma: https://www.figma.com/design/hpgrupgalhhqBGUlCttl6m/  
- RuStore каталог: https://www.rustore.ru/catalog/app/maks.molch.dmitr.makslauncher  
- Отзывы: https://www.rustore.ru/catalog/app/maks.molch.dmitr.makslauncher/reviews  
- Console: https://console.rustore.ru/apps/2063541835  
