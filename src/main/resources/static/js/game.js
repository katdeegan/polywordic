// Game constants
const WORD_LENGTH = 5;

// Game state variables
let gameId = null;
let currentRow = 0;
let currentTile = 0;
let currentGuess = '';
let gameOver = false;
let maxAttempts = 6;
let selectedDifficulty = null;

// TODO: update to call backend endpoints (defined in controller/PolywordicController.java)
// TODO: frontend game logic: adding letters, submitting guesses, displaying results

// handles difficulty button selection and creates new game
function handleDifficultySelection(difficulty) {
    selectedDifficulty = difficulty;
    showMessage('Creating game...', 'info');
    createNewGame(difficulty);
}

// Calls backend API to create game at desired difficulty level
async function createNewGame(difficulty) {
    try {
        const response = await fetch(`/api/game/new?difficulty=${difficulty}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to create game');
        }

        const data = await response.json();

        // Store game information
        gameId = data.gameId;
        maxAttempts = data.maxAttempts;

        console.log('Game created:', data);

        startGame();

    } catch (error) {
        console.error('Error creating game:', error);
        showMessage('Error creating game. Please try again.', 'error');
    }
}

// Initializes UI for new game
function startGame() {
    document.getElementById('difficulty-selection').classList.add('hidden'); // hide difficulty selection screen
    document.getElementById('game-screen').classList.remove('hidden'); // show game screen

    const subtitle = document.getElementById('subtitle');
    subtitle.textContent = `${selectedDifficulty}: Guess a 5-letter word with ${maxAttempts} chances!`;

    initializeBoard();

    // Reset game state
    currentRow = 0;
    currentTile = 0;
    currentGuess = '';
    gameOver = false;

    showMessage('', '');

    updateGuessButton();
}

// Initialize game board - empty tile rows based on difficulty level
function initializeBoard() {
    const gameBoard = document.getElementById('game-board');
    gameBoard.innerHTML = '';

    for (let i = 0; i < maxAttempts; i++) {
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

// Reset game - when "New Game" is pressed
function resetGame() {
    // hide game screen and show difficulty selection
    document.getElementById('game-screen').classList.add('hidden');
    document.getElementById('difficulty-selection').classList.remove('hidden');

    // Reset subtitle
    document.getElementById('subtitle').textContent = 'Select a difficulty level to start';

    // Reset game state
    gameId = null;
    currentRow = 0;
    currentTile = 0;
    currentGuess = '';
    gameOver = false;
    selectedDifficulty = null;

    showMessage('', '');

    resetKeyboard();
}

// TODO: Reset keyboard colors
function resetKeyboard() {
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {

    // Difficulty selection buttons
    document.querySelectorAll('.difficulty-btn').forEach(button => {
        button.addEventListener('click', () => {
            const difficulty = button.getAttribute('data-difficulty');
            handleDifficultySelection(difficulty);
        });
    });

    // UI Keyboard click events
    document.querySelectorAll('.key').forEach(key => {
        key.addEventListener('click', () => {
            const keyValue = key.getAttribute('data-key');
            handleKeyPress(keyValue);
        });
    });

    // Physical keyboard events
    document.addEventListener('keydown', (e) => {
        // Only handle keyboard if game is active
        if (!gameId || gameOver) return;

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

    // New game button - returns to difficulty selection
    document.getElementById('new-game-btn').addEventListener('click', () => {
        resetGame();
    });
});