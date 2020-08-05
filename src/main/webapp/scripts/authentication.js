
// Run authenticate function on page load. 
window.onload = function() {
  authenticate();
};

/**
 * Make a request to Authentication servlet to verify whether user is signed
 * in. If it is users first time signing in, a user entity will be made.
 */
function authenticate() {
  fetch('/authentication')
  .then(response => response.json())
  .then(authenticationInfo => {
    console.log(authenticationInfo);

    // Determine if user is logged in or not 
    let userIsLoggedIn = (authenticationInfo.userIsLoggedIn === 'true');

    const authenticationLink = document.getElementById('authentication-link');

    if (userIsLoggedIn) {
      // Create logout link.
      authenticationLink.href = authenticationInfo.logoutLink;
      authenticationLink.innerText = "Logout";
    } else {
      // Create login link.
      authenticationLink.href = authenticationInfo.loginLink;
      authenticationLink.innerText = "Login";
      // Here is an example of changes that can be made if the user is not logged in.
      hideUserNavIcons();
      // In the case of user.html and newlisting.html, we can use something
      // like window.location.replace("/index.html"); to keep non-signed in
      // users from navigating to them manually in addition to a quick back-end
      // check when a fetch request is made.
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
