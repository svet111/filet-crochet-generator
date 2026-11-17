# Filet Crochet Pattern Generator

I'm really into crocheting right now, and whenever I wanted to make a filet crochet piece from a picture or pixel art, I realized how much time it takes to either hunt down the exact pattern online or count squares by hand on graph paper.

So I built this web app to turn any image into a ready-to-crochet filet pattern in seconds.

## What It Does

* **Image-to-Grid Conversion:** Upload a PNG or JPG, pick your stitch width (5–150 stitches), and choose between **Smooth** resizing (for photos) or **Sharp** nearest-neighbor scaling (for pixel art and crisp logos).
* **Adjustable Contrast Threshold:** Slide the brightness cutoff (`0–255`) live so light sketches or high-contrast icons both translate cleanly into open mesh (`□`) and filled double-crochet blocks (`■`).
* **Real Filet Crochet Row Order:** Automatically reads from the **bottom row up** and alternates direction every row—odd rows Right-to-Left (`RS ←`) and even rows Left-to-Right (`WS →`)—so you don't have to mentally flip the chart while working.
* **Built-in Row Counter:** Check off rows as you crochet, track your progress, and highlight your active row directly on the visual chart.
* **Save as PDF / Print:** One-click print layout that strips out the UI controls so you can save a clean pattern sheet to your tablet or print it out.

## Tech Stack

* **Backend:** Java 21, Spring Boot (Spring Web), `javax.imageio` / `Graphics2D`
* **Frontend:** Vanilla HTML, CSS Grid, JavaScript (`fetch` API)
* **Testing:** JUnit 5


## Running It Locally

### Option 1: Run the Standalone JAR (Fastest)
If you just want to use the app without building from source:
1. Download `crochet-generator-0.0.1-SNAPSHOT.jar` from the [Releases](https://github.com/YOUR_USERNAME/crochet-generator/releases) page.
2. Make sure you have **Java 21+** installed, then run:
   ```bash
   java -jar crochet-generator-0.0.1-SNAPSHOT.jar

    ```

3. Open `http://localhost:8080` in your browser and start crocheting!

### Option 2: Build & Run from Source

1. Clone the repository:
```bash
git clone (https://github.com/svet111/crochet-generator.git)
cd crochet-generator

```


2. Run directly with the included Maven wrapper:
```bash
./mvnw spring-boot:run

```


*(Or build your own `.jar` anytime with `./mvnw clean package`)*

