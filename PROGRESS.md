# Mnemra Progress

> **Capture what matters. Remember what lasts.**

---

# Overview

Mnemra is an Android knowledge-retention application built to help users preserve valuable information discovered while browsing the web and social media.

Instead of collecting bookmarks that are rarely revisited, Mnemra converts captured content into structured memories that can later be reviewed using flashcards and spaced repetition.

The project is guided by one product principle:

> **Capture first. Organize later.**

The user should never be forced to organize information before it is safely captured.

---

# Current Release

**Version:** v0.1 Stable

Status:

- ✅ Stable architecture
- ✅ Stable database
- ✅ Stable navigation
- ✅ Functional capture workflow
- ✅ Functional review workflow

The application is considered **feature-complete for the v0.1 functional core**, but is **not yet ready for daily use** because capture support outside YouTube is still limited.

---

# Goals

Primary goals:

- Capture useful information in seconds.
- Reduce friction during capture.
- Encourage long-term retention instead of passive bookmarking.
- Build a personal knowledge base that grows naturally over time.

---

# Technology Stack

- Kotlin
- Jetpack Compose
- Material 3
- Room Database
- Hilt Dependency Injection
- Coroutines
- Kotlin Flow
- Navigation Compose

Architecture:

```
UI
↓
ViewModel
↓
Repository
↓
Room
↓
SQLite
```

---

# Implemented Features

## Memory System

- Manual memory creation
- Memory editing
- Memory deletion
- Favorites
- Archive
- Restore
- Search
- Reactive updates using Flow

---

## Source System

Each captured memory may reference a source.

Implemented:

- Source title
- Source URL
- Source type
- Open original source

Currently supported:

- YouTube

---

## Capture System

Implemented:

- Android Share Target
- Source reuse
- Empty capture reuse
- Duplicate prevention
- URL canonicalization for YouTube

Supported URL formats:

- youtube.com/watch
- youtube.com/shorts
- youtu.be

---

## Flashcards

Implemented:

- Create
- Delete
- Link to Memory
- Expand long questions
- Review individual flashcards

---

## Review

Implemented:

- Again
- Remembered
- Due queue
- Review history
- Basic scheduling

Current scheduling is intentionally simple.

FSRS is planned for a later release.

---

## Navigation

Screens:

- Home
- Create Memory
- Memory Detail
- Archive
- Create Flashcard
- Review Flashcard
- Review Queue

Navigation uses a single-activity architecture with Navigation Compose.

---

# Database

Current entities:

- Source
- Memory
- Flashcard
- Review
- Tag
- MemoryTag
- CollectionEntity
- MemoryCollection

Relationships:

```
Source
    │
    └────────► Memory
                    │
                    ├────────► Flashcard
                    │                │
                    │                └────────► Review
                    │
                    ├────────► MemoryTag
                    │
                    └────────► MemoryCollection
```

Tags and Collections currently exist in the schema but are not exposed in the UI.

---

# Engineering Decisions

## MVVM

Chosen for simplicity and maintainability.

---

## Hilt

Chosen to eliminate manual dependency wiring and simplify ViewModel creation.

---

## Room

Chosen over raw SQLite for:

- Type safety
- Compile-time verification
- Reactive Flow support

---

## Capture Philosophy

Capture should require the smallest amount of user effort possible.

Mandatory:

- URL **or**
- Manual content

Everything else is optional during capture.

Examples:

- Title
- Tags
- Collections
- Flashcards

These can all be added later.

---

# Major Problems Solved During v0.1

## Architecture

- Repository layer introduced
- Hilt migration completed
- Navigation refactored

---

## Database

- Main-thread Room crashes
- Reactive DAO conversion
- Foreign key cleanup

---

## Navigation

- Duplicate navigation
- White-screen regressions
- Route validation
- Back stack stabilization

---

## Capture

- Duplicate empty captures
- Source reuse
- YouTube URL normalization

---

## UI

- Scrollable screens
- Flashcard expansion
- Long-content handling
- Save-state stabilization
- Duplicate save prevention

---

# Known Limitations

## Capture

Instagram support is incomplete.

Instagram shares captions differently from YouTube and requires its own parsing strategy.

Future platform adapters are planned.

---

## Review

Current scheduling is intentionally basic.

FSRS has not yet been implemented.

---

## Organization

Tags are not yet available.

Collections are not yet available.

---

## Home Screen

Content preview is basic.

Additional polish is planned.

---

# Development Principles

Every new feature should strengthen the core workflow:

```
Discover
      ↓
Capture
      ↓
Remember
      ↓
Review
      ↓
Retain
```

Features that do not improve this loop should have lower priority.

---

# Roadmap

## v0.2

### Capture Expansion

Priority:

- Instagram adapter
- Better share parsing
- Better source detection
- Capture UX improvements

Goal:

Support daily use across major content platforms.

---

## v0.3

### Organization

- Tags
- Tag filtering
- Collections
- Collection browsing

---

## v0.4

### Review Improvements

- Statistics
- Retention metrics
- Review insights
- Better scheduling

---

## v0.5

### Intelligent Learning

- FSRS scheduling
- AI flashcard generation
- AI summarization
- Adaptive review

---

# Future Ideas

Potential future features:

- Browser extension
- Desktop application
- OCR capture
- PDF import
- Image support
- Cloud sync
- Backup & Restore
- Import / Export
- AI-assisted memory extraction

---

# Repository Structure

```
app/
├── data/
│   ├── dao/
│   ├── database/
│   ├── entity/
│   └── repository/
│
├── di/
│
├── ui/
│   ├── navigation/
│   └── screen/
│
├── viewmodel/
│
└── ShareActivity.kt
```

---

# Release History

## v0.1 Stable

Completed:

- Compose application setup
- Room integration
- Repository architecture
- Hilt dependency injection
- Navigation Compose
- Share Target
- Source abstraction
- Memory CRUD
- Search
- Favorites
- Archive
- Flashcards
- Review queue
- Basic spaced repetition
- Duplicate save prevention
- Navigation stabilization
- YouTube URL canonicalization

---

# Immediate Priorities

1. Instagram capture support
2. Capture adapters
3. Home screen polish
4. Tags
5. Collections
6. Review statistics
7. FSRS scheduling

---

# Current Assessment

Architecture: **Stable**

Database: **Stable**

Navigation: **Stable**

Capture: **Stable for YouTube**

Review: **Functional**

Overall project maturity:

**v0.1 Stable**

The engineering foundation is complete. Future development should focus on expanding capture capabilities, improving usability, and enhancing long-term learning features rather than rebuilding core architecture.