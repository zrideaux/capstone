
/**
 * Update a button when it is clicked.
 */
function toggleClicked(vote) {
  buttonClicked = document.getElementById('reputation-' + vote);
  // Check if a button is already clicked 
  
  // Darken/lighten the button
  buttonClicked.classList.toggle('reputation-button-clicked');
}

/**
 * Send a request to Reputation servlet to update a lists reputation
 */
function updateReputation() {

}