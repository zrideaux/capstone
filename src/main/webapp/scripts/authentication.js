/**
 * Make a request to Authentication servlet to verify whether user is signed
 * in. If it is users first time signing in, a user entity will be made.
 *
 * @param loggedInActions a function to be called if a user is logged in
 * @param loggedOutActions a function to be called if a user is logged out
 */
function authenticate(loggedInActions = () => {}, loggedOutActions = () => {}) {
  fetch('/authentication')
  .then(response => response.json())
  .then(authenticationInfo => {
    console.log(authenticationInfo);

    // Determine if user is logged in or not 
    let userIsLoggedIn = (authenticationInfo.userIsLoggedIn === 'true');

    const authenticationLink = document.getElementById('authentication-link');
    console.log(authenticationLink);
    if (userIsLoggedIn) {
      // Perform specified then default actions for logged in users
      loggedInActions();
      // Create logout link.
      authenticationLink.href = authenticationInfo.logoutLink;
      authenticationLink.innerText = "Logout";
    } else {
      // Perform specified then default actions for logged out users
      loggedOutActions();
      // Create login link.
      authenticationLink.href = authenticationInfo.loginLink;
      authenticationLink.innerText = "Login";
      // Find all page elements with 'user-only' class and set display to none
      let userOnlyElements = document.getElementsByClassName('user-only');
      console.log(userOnlyElements);
      for (let i = 0; i < userOnlyElements.length; i++) {
        userOnlyElements[i].style.display = 'none';
      }
    }
  });
}

/**
 * Hide navbar icons that require a user to be logged in to use. 
 */
function hideUserNavIcons() {
  // Find all page elements with 'user-only' class and set display to none
  let userOnlyElements = document.getElementsByClassName('user-only');

  for (let i = 0; i < userOnlyElements.length; i++) {
    userOnlyElements[i].style.display = 'none';
  }
}

export { authenticate };