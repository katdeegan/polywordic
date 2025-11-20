// Game constants
const MAX_ATTEMPTS = 6;
const WORD_LENGTH = 5;

// TODO: variables for storing game state (currentRow, currentTile, currentGuessNumber, gameOver, etc.)

// Initialize game board - rows are generated dynamically as user guesses letters
function initializeBoard() {
    const gameBoard = document.getElementById('game-board');
    gameBoard.innerHTML = '';

    for (let i = 0; i < MAX_ATTEMPTS; i++) {
        const row = document.createElement('div');
        row.className = 'row';
        row.id = `row-${i}`;

        for (let j = 0; j < WORD_LENGTH; j++) {
            const tile = document.createElement('div');
            tile.className = 'tile';
            tile.id = `tile-${i}-${j}`;
            row.appendChild(tile);
        }

        gameBoard.appendChild(row);
    }
}

// TODO: possible functions we may or may not need

// TODO: test / fix logic
// Handle key press
function handleKeyPress(key) {
    if (gameOver) {
        return;
    }

    if (key === 'BACKSPACE') {
        deleteLetter();
    } else if (currentTile < WORD_LENGTH) {
        addLetter(key);
    }

    updateGuessButton();
}

// TODO: Add letter to current guess
function addLetter(letter) {
}

// TODO: Delete last letter
function deleteLetter() {
}

// Updates "Guess Word" button state
function updateGuessButton() {
    const guessBtn = document.getElementById('guess-btn');
    if (currentGuess.length === WORD_LENGTH) {
        guessBtn.disabled = false;
    } else {
        guessBtn.disabled = true;
    }
}

// TODO: Submit guess
function submitGuess() {
}

// TODO: test
// Shake animation for invalid guess
function shakeTiles() {
    const row = document.getElementById(`row-${currentRow}`);
    row.style.animation = 'none';
    setTimeout(() => {
        row.style.animation = 'shake 0.5s';
    }, 10);
}

// TODO: message displayed to user (i.e. for correct / incorrect guesses, when game is over)
// Show message to user
function showMessage(text, type) {
    const messageElement = document.getElementById('message');
    messageElement.textContent = text;
    messageElement.className = `message ${type}`;
}

// TODO: when "New Game" button is pressed
// Reset game
function resetGame() {
}

// TODO: Reset keyboard colors
function resetKeyboard() {
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    initializeBoard();
    updateGuessButton();

    // UI Keyboard click events
    document.querySelectorAll('.key').forEach(key => {
        key.addEventListener('click', () => {
            const keyValue = key.getAttribute('data-key');
            handleKeyPress(keyValue);
        });
    });

    // Physical keyboard events
    document.addEventListener('keydown', (e) => {
        if (gameOver) return;

        const key = e.key.toUpperCase();

        if (key === 'ENTER') {
            if (currentGuess.length === WORD_LENGTH) {
                submitGuess();
            }
        } else if (key === 'BACKSPACE') {
            handleKeyPress('BACKSPACE');
        } else if (/^[A-Z]$/.test(key)) {
            handleKeyPress(key);
        }
    });

    // Guess button click
    document.getElementById('guess-btn').addEventListener('click', () => {
        submitGuess();
    });

    // New game button
    document.getElementById('new-game-btn').addEventListener('click', () => {
        resetGame();
    });
});