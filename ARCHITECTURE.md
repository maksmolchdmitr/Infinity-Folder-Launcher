# Architecture — Infinity Folder Launcher

Короткий ориентир для агента/разработки. Детали задач — в `TASKS.md`.

## Стек

- Kotlin, Jetpack Compose, Material3
- Home Activity (`MAIN` + `HOME` + `DEFAULT` + `LAUNCHER`)
- Хранение папок: `SharedPreferences` + Gson (`FolderDao`), schema v1
- `applicationId` (RuStore): `maks.molch.dmitr.makslauncher`

## Экраны (`Screen`)

| Screen | Файл | Назначение |
|--------|------|------------|
| Splash | `ui/screen/Splash.kt` | короткий сплэш → Onboarding или Main |
| Onboarding | `ui/screen/Onboarding.kt` | 3 шага, флаг в `OnboardingDao` |
| Main | `ui/screen/Main.kt` | сетка текущей папки, edit/move/clear |
| AddFolder | `ui/screen/AddFolder.kt` | создать дочернюю папку |
| AddApplication | `ui/screen/AddApplication.kt` | добавить установленные приложения |
| AddWidget | заглушка → возврат на Main | фаза 4 |

Один `MainActivity`, навигация экранов через `MutableState<Screen>`.

## Навигация по папкам

- Стек id: `folderStack: SnapshotStateList<String>`, корень = `MAIN_FOLDER_ID` (`"main"`)
- Открыть папку: `folderStack.add(folder.id)`
- Назад: `BackHandler` снимает последний id; на корне back глотается (лаунчер не закрывается)
- **Не** стартуем новую Activity на каждую папку

## Модель (`data/LauncherObject.kt`)

```
LauncherObject
  id: String
  name: String
├── Folder(id, name, launcherObjects, iconName?)
└── Application(id = packageName, name, packageName)
```

- У папок id — UUID (корень фиксирован `"main"`)
- У приложений id = `packageName`
- В родителе вложенная папка хранится как **reference** (`launcherObjects` пустой); полное содержимое — отдельная запись в prefs по id

## DAO

| Класс | Роль |
|-------|------|
| `FolderDao` | CRUD папок, `epoch: StateFlow` для реактивного UI, миграция schema → 1 |
| `ApplicationDao` | список установленных приложений через `PackageManager` |
| `OnboardingDao` | флаг прохождения онбординга |

Мутации `FolderDao` инкрементят `epoch`; экраны делают `collectAsState()` и перечитывают папку.

Prefs: `Folders` (ключ = folder id), `FolderMeta.schema_version`.

## Ключевые UI-компоненты

- `ObjectCell` — иконка + имя, selection в edit mode (по `id`)
- `NavBar` — Home / Add App / Add Folder / Widget
- `SelectFolder` + `FolderSearch` — выбор цели для move
- `TopBar`, `ConfirmRemove`, theme в `ui/theme/`

## Сборка без Studio

```bash
./scripts/build-debug.sh
```

JDK 21 + `~/Library/Android/sdk`.
