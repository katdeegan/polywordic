# Polywordic

A Wordle-inspired Java web application built using **Spring Boot**, **Thymeleaf**, and **Gradle**.

---

## Team Members

- Katherine Deegan
- Yifei Zhang

---

## Project Description

---

## Object-Oriented Design Patterns Used

### 1. Model-View-Controller (MVC)

_Description:_

_Implementation:_

- Model file(s): [`polywordic/model`](src/main/java/com/ooad_kd_yz/polywordic/model)
- View file(s): [`resources/templates`](src/main/resources/templates)
- Controller file(s): [`polywordic/controller`](src/main/java/com/ooad_kd_yz/polywordic/controller)

### 2. Iterator

_Description:_

Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation by implementing Iterator and Iterable interfaces.

_Implementation:_

- Iterator Interface: [`polywordic/interface/PWIterator.java`](src/main/java/com/ooad_kd_yz/polywordic/model/iterator/PWIterator.java)
  - The concrete iterator object [**PolywordicWordIterator**](src/main/java/com/ooad_kd_yz/polywordic/model/iterator/PolywordicWordIterator.java) implements the **PWIterator interface**.
- Iterable Interface: [`polywordic/interface/PWIterable.java`](src/main/java/com/ooad_kd_yz/polywordic/model/iterator/PWIterable.java)
  - The aggregate object [**PolywordicWord**](src/main/java/com/ooad_kd_yz/polywordic/model/PolywordicWord.java) (composed of [**PolywordicLetter**](src/main/java/com/ooad_kd_yz/polywordic/model/PolywordicLetter.java) objects) and implements the **PWIterable** interface.
    |

### 3. State

_Description:_

This pattern enables an object to alter its behavior in response to internal state changes, making it appear as if the object has changed classes.
To accomplish this, a **Context** object maintains an instance of a **State** object. The **State** object encapsulates behaviors linked to various conditions.
In the case of Polywordic, the State objects encapsulate the handling of a player's word guess, which changes based on if the game is in an Active or Over (Won or Lost) state.

_Implementation:_

- Context: [`polywordic/state/GameStateContext.java`](src/main/java/com/ooad_kd_yz/polywordic/state/GameStateContext.java)
- State Interface: [`polywordic/state/IGameState.java`](src/main/java/com/ooad_kd_yz/polywordic/state/IGameState.java)
- Concrete States: [`ActiveGameState.java`](src/main/java/com/ooad_kd_yz/polywordic/state/ActiveGameState.java), [`WonGameState.java`](src/main/java/com/ooad_kd_yz/polywordic/state/WonGameState.java), [`LostGameState.java`](src/main/java/com/ooad_kd_yz/polywordic/state/LostGameState.java)

### Run Polywordic Locally

From terminal (in polywordic/ directory) run: `./gradlew bootRun`

**OR**, from the file navigator, run: [PolywordicApplication.java](src/main/java/com/ooad_kd_yz/polywordic/PolywordicApplication.java)

Then, navigate to `http://localhost:8080` in your browser.
