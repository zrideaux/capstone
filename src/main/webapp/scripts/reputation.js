/**
 * Calls functions to perform whenever a reputation button is clicked.
 *
 * @param buttonClicked the DOM element of the button that was clicked
 * @param vote string representing the value of a vote (upvote, 
 *    neutral, downvote)
 * @param key string representing the key of a listing
 */
function voteClicked(buttonClicked, vote, key) {
  // Update reputation in the backend
  updateReputation(vote, key);
  
  // Update the front end to let the user know their input went through
  updateNumbers(buttonClicked, vote, key);
  
  let downvoteButton = document.getElementById('reputation-downvote-' + key);
  let upvoteButton = document.getElementById('reputation-upvote-' + key);
  let downvotePreviewButton = document.getElementById('reputation-downvote-preview-' + key);
  let upvotePreviewButton = document.getElementById('reputation-upvote-preview-' + key);

  toggleClicked(buttonClicked, downvoteButton, downvotePreviewButton,
      upvoteButton, upvotePreviewButton);
  
  // Update the onclick listeners depending on what vote was received
  if (vote === 'upvote') {
    setOnclicks(downvoteButton, 'downvote', upvoteButton, 'neutral', key);
  } else if (vote === 'downvote') {
    setOnclicks(downvoteButton, 'neutral', upvoteButton, 'upvote', key);
  } else if (vote === 'neutral') {
    setOnclicks(downvoteButton, 'downvote', upvoteButton, 'upvote', key);
  }
}

/**
 * Updates the initial state of the vote buttons based on previous interactions.
 *
 * @param downvoteButton a listing's downvote button
 * @param upvoteButton a listing's upvote button
 * @param existingVote a user's prior vote on a listing
 * @param key the listing's key
 */
function setInitialVoteState(downvoteButton, upvoteButton, existingVote, key) {
  let isActualButton = !(downvoteButton.classList.contains('reputation-preview')
      || upvoteButton.classList.contains('reputation-preview'));
  
  if (existingVote === 'upvote') {
    upvoteButton.classList.toggle('reputation-button-clicked');
    if (isActualButton) {
      setOnclicks(downvoteButton, 'downvote', upvoteButton, 'neutral', key);
    }
  } else if (existingVote === 'downvote') {
    downvoteButton.classList.toggle('reputation-button-clicked');
    if (isActualButton) {
      setOnclicks(downvoteButton, 'neutral', upvoteButton, 'upvote', key); 
    }
  } else if (existingVote === 'neutral') {
    if (isActualButton) {
      setOnclicks(downvoteButton, 'downvote', upvoteButton, 'upvote', key);
    }
  }
}

/**
 * Updates the onclick functions of the vote buttons when called.
 *
 * @param downvoteButton the HTML element for a listing's downvote button
 * @param downvoteValue the vote value to send when the downvote button is clicked
 * @param upvoteButton the HTML element for a listing's upvote button
 * @param upvoteValue the vote value to send when the upvote button is clicked
 * @param key the key for the listing the buttons are attached to
 */
function setOnclicks(downvoteButton, downvoteValue, upvoteButton, upvoteValue, key) {
  downvoteButton.onclick = () => {
    voteClicked(downvoteButton, downvoteValue, key);
  }

  upvoteButton.onclick = () => {
    voteClicked(upvoteButton, upvoteValue, key);
  }
}

/**
 * Updates the numbers in a listing's voting section for the client
 * when a button is clicked.
 *
 * @param buttonClicked the button that was clicked to call this function
 * @param vote the value of the vote sent from the button
 * @param key the key of the listing whose buttons were clicked
 */
function updateNumbers(buttonClicked, vote, key) {
  if (vote === 'upvote') {
    // Increment the upvote counter
    let upvoteCount = document.getElementById('reputation-count-upvotes-' + key);
    let upvotePreviewCount = document.getElementById(
        'reputation-preview-count-upvotes-' + key);
    upvoteCount.innerText = parseInt(upvoteCount.innerText) + 1;
    upvotePreviewCount.innerText = parseInt(upvotePreviewCount.innerText) + 1;

    // Decrement the downvote counter if it was previously clicked
    let downvoteCount = document.getElementById(
        'reputation-count-downvotes-' + key);
    let downvotePreviewCount = document.getElementById(
        'reputation-preview-count-downvotes-' + key);
    if (downvoteCount.parentElement.className.includes('reputation-button-clicked')) {
      downvoteCount.innerText = parseInt(downvoteCount.innerText) - 1;
      downvotePreviewCount.innerText = parseInt(downvotePreviewCount.innerText) - 1;
    }
  } else if (vote === 'downvote') {
    // Increment the downvote counter
    let downvoteCount = document.getElementById('reputation-count-downvotes-' + key);
    let downvotePreviewCount = document.getElementById(
        'reputation-preview-count-downvotes-' + key);
    downvoteCount.innerText = parseInt(downvoteCount.innerText) + 1;
    downvotePreviewCount.innerText = parseInt(downvotePreviewCount.innerText) + 1;
    
    // Decrement the upvote counter if it was previously clicked
    let upvoteCount = document.getElementById(
        'reputation-count-upvotes-' + key);
    let upvotePreviewCount = document.getElementById(
        'reputation-preview-count-upvotes-' + key);
    if (upvoteCount.parentElement.className.includes('reputation-button-clicked')) {
      upvoteCount.innerText = parseInt(upvoteCount.innerText) - 1;
      upvotePreviewCount.innerText = parseInt(upvotePreviewCount.innerText) - 1;
    }
  } else if (vote === 'neutral') {
    let clickedCount = buttonClicked.querySelector(".reputation-count");
    let clickedPreviewCount;
    if (clickedCount.id.includes('downvote')) {
      clickedPreviewCount = document.getElementById(
          'reputation-preview-count-downvotes-' + key);
    } else if (clickedCount.id.includes('upvote')) {
      clickedPreviewCount = document.getElementById(
          'reputation-preview-count-upvotes-' + key);
    }

    // If the vote is neutral, decrement the clicked button
    clickedCount.innerText =
        parseInt(clickedCount.innerText) - 1;
    clickedPreviewCount.innerText =
        parseInt(clickedPreviewCount.innerText) - 1;
  }
}

/**
 * Toggle the clicked class on clicked and unclicked buttons.
 *
 * @param buttonClicked the button that was clicked to call this function
 * @param downvoteButton the listing's downvote button element
 * @param downvotePreviewButton the listing's preview downvote button element
 * @param upvoteButton the listing's upvote button element
 * @param upvotePreviewButton the listing's preview upvote button element
 */
function toggleClicked(buttonClicked, downvoteButton, downvotePreviewButton,
    upvoteButton, upvotePreviewButton) {
  // Add/remove the clicked class to whatever button is clicked
  buttonClicked.classList.toggle('reputation-button-clicked');

  // If a button besides the one clicked has the clicked class, remove it
  if (buttonClicked === downvoteButton) {
    downvotePreviewButton.classList.toggle('reputation-button-clicked');
    if (buttonIsClicked(upvoteButton)) {
      upvoteButton.classList.toggle('reputation-button-clicked');
      upvotePreviewButton.classList.toggle('reputation-button-clicked');
    }
  } else if (buttonClicked === upvoteButton) {
    upvotePreviewButton.classList.toggle('reputation-button-clicked');
    if (buttonIsClicked(downvoteButton)) {
      downvoteButton.classList.toggle('reputation-button-clicked');
      downvotePreviewButton.classList.toggle('reputation-button-clicked');
    }
  }
}

/**
 * Returns a boolean representing if a button has the clicked class.
 * 
 * @param button the button to check for the clicked class
 * @return true if button has clicked class or false if it doesn't
 */
function buttonIsClicked(button) {
  if (button.className.includes('reputation-button-clicked')) {
    return true;
  }
  return false;
}

/**
 * Send a request to Reputation servlet to update a lists reputation
 */
function updateReputation(vote, key) {
  fetch('/reputation?key=' + key + '&vote=' + vote, {method: 'post'});
}

export {
  setInitialVoteState,
  voteClicked
}
