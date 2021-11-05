const prompt = require('prompt-sync')();

// main function that initiates nQueens problem algorithm
function nQueens(n) {
  return getNQueens(n, 0, [], [])
}

// gets all possible solutions for n queens on n x n board
function getNQueens(n, row, positions, validPositions) {
  // check if at the end of the board
  if(row === n) {
    const board = generateBoard(positions);
    validPositions.push(board);

    return validPositions
  } else {
    // loop through positions on board to check for valid positions to place queen
    // uses a stack to evaluate current position
    for(let i = 0; i < n; i++) {
      positions.push(i);
      if(isValid(positions)) {
        validPositions = getNQueens(n, row+1, positions, validPositions);
      }
      // pop position from stack to move to the next position in board
      positions.pop();
    }
  }

  return validPositions
}

// create board with valid queen positions
function generateBoard(pos) {
  const boardPositions = [];

  for(let i = 0; i < pos.length; i++) {
    boardPositions.push('');
  }

  for(let i = 0; i < boardPositions.length; i++) {
    for(let j = 0; j < boardPositions.length; j++) {
      if(pos[i] === j) boardPositions[i] += 'Q';
      else boardPositions[i] += '.';
    }

  }
  return boardPositions
}

// n-queens algorithm
// check that position does not have a queen, is not in the same row, column, or diagonal of another queen
function isValid(pos) {
  const row = pos.length-1;
  const column = pos[pos.length-1];

  for(let i = 0; i < pos.length-1; i++) {
    const currentRow = i;
    const currentCol = pos[i];
    const leftDiagonal = currentCol - (row - currentRow);
    const rightDiagonal = currentCol + (row - currentRow);

    if((column === currentCol) || (column === leftDiagonal || column === rightDiagonal)) {
      return false;
    }
  }
  return true;
}

// prompt user for input n
const n = parseInt(prompt('Enter value for n: '));

// check if valid input
if (n < 1 || n > 4 ) {
  console.log('n must be greater than 0 and less than 5');
  return;
} else {
  console.log(nQueens(n));
  // generate cnf
}
