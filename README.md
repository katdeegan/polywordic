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

### Run Polywordic Locally

From terminal (in polywordic/ directory) run: `./gradlew bootRun`

**OR**, from the file navigator, run: [PolywordicApplication.java](src/main/java/com/ooad_kd_yz/polywordic/PolywordicApplication.java)

Then, navigate to `http://localhost:8080` in your browser.
