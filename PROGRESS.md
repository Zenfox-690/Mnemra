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

**Version:** v2.0.0 Stable

Status:

- ✅ Platform-agnostic capture architecture
- ✅ Support for YouTube, Instagram, and general Websites
- ✅ Spaced repetition review system
- ✅ Incomplete capture management
- ✅ Robust navigation and state management

The application is now ready for daily use as a personal knowledge capture and retention tool.

---

# Technology Stack

- Kotlin
- Jetpack Compose
- Material 3
- Room Database
- DataStore Preferences
- Hilt Dependency Injection
- Coroutines & Flow
- Navigation Compose

---

# Implemented Features

## Capture System

- **Universal Share Target:** Capture from any app.
- **Capture Adapters:** Specialized logic for YouTube, Instagram, and Websites.
- **URL Canonicalization:** Prevents duplicates from mobile/desktop/shorts URLs.
- **Silent Capture:** Captures in the background by default to minimize friction.
- **Optional Prompts:** Users can enable a note-taking prompt for specific platforms.

## Memory Management

- **Incomplete Captures:** Surfaces recent captures that need context.
- **Full CRUD:** Create, Read, Update, Delete memories.
- **Favorites & Archive:** Organize and clean up your feed.
- **Search:** Instant reactive search across all memories.

## Learning System

- **Flashcards:** Create Q&A pairs for any memory.
- **Review Queue:** Spaced repetition system that surfaces due cards.
- **Review History:** Tracks performance for scheduling.

---

# Engineering Decisions

- **Adapter Pattern:** Platform-specific logic is isolated from the repository.
- **Derived State:** "Incomplete" status is calculated from data, not stored as a flag.
- **Reactive UI:** Compose + Flow ensures the UI always matches the database.
- **Transactional Safety:** Capture logic uses Room transactions to ensure consistency.

---

# Roadmap

## v3.0

### Organization

- **Tags:** Categorize memories with labels.
- **Collections:** Group related memories together.
- **Rich Filtering:** Combine tags, collections, and sources.

## v4.0

### Spaced Repetition (FSRS)

- **FSRS Algorithm:** Implement modern spaced repetition for better retention.
- **Learning Statistics:** Visualize your retention and progress.
- **Review Insights:** Identify difficult concepts.

## v5.0

### AI Assistance

- **Auto-Summarization:** Generate memory titles/content from sources.
- **Flashcard Generation:** AI-suggested questions and answers.
- **Semantic Search:** Find memories by meaning, not just keywords.

---

# Release History

## v2.0.0 Stable
- Implemented Adapter-based capture architecture.
- Added Instagram support.
- Introduced "Incomplete Captures" section.
- Added Settings with DataStore.
- Finalized v2 polishing.

## v1.0.0 Stable
- Initial release with YouTube support.
- Core Memory and Flashcard systems.
- Basic Review queue.
- Hilt and Room foundation.
