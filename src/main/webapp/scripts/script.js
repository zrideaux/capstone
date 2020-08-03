
// Functions to handle authentication

let auth2;

/**
 * Run on page load. Sets up authentication.
 */
function init() {
  const CLIENT_ID = 'client_id_goes_here';
  
  gapi.load('auth2', function() {
    GoogleAuth = gapi.auth2.init({client_id: CLIENT_ID})
    .then(() => {
      renderButton();
    });
  });
}

/**
 * Places the sign in button on the page.
 */
function renderButton() {
  gapi.signin2.render('signInButton', {
    'scope': 'profile email',
    'width': 180,
    'height': 40,
    'longtitle': false,
    'theme': 'light',
    'onsuccess': onSignIn,
  });
}

/**
 * Attaches a google sign in link to a specified element.
 *
 * @param element an html element
 */
function attachSignin(element) {
  console.log(element.id);
  GoogleAuth.attachClickHandler(
    element,
    {},
    function(googleUser) {
      document.getElementById('name').innerText = "Signed in: " +
          googleUser.getBasicProfile().getName();
    },
    function(error) {
      alert(JSON.stringify(error, undefined, 2));
    }
  );
}

/**
 * Actions to perform when user first signs in or on page load if
 * user is already signed in.
 */
function onSignIn(googleUser) {
  let userProfile = googleUser.getBasicProfile();
  let idToken = googleUser.getAuthResponse().id_token;
  console.log('Email: ' + userProfile.getEmail());
  
  // Send user's token to CreateUser servlet.
  const createUserUrl = '/create-user';
  const http = new XMLHttpRequest();

  http.open('POST', createUserUrl, true);
  http.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  
  http.onreadystatechange = function () {
    if (http.readyState == 4 && http.status == 200) {
      console.log(http.responseText);
    } else if (http.status >= 400) {
      console.log(http.responseText)
    }
  }
  
  http.send('idtoken=' + idToken);
}

/**
 * An example of how to use the front-end portion of the user authentication.
 */
function example() {
  // Get the current user and their token.
  let googleUser = GoogleAuth.currentUser.get();
  let idToken = googleUser.getAuthResponse().id_token;

  // Use getBasicProfile() to get allowed properties of the account.
  console.log(googleUser.getBasicProfile().getEmail());
  
  // Do a quick front end check to make sure they're signed in.
  // If they aren't, you might want to do something else like hide
  //     HTML elements or that kind of thing.
  if (GoogleAuth.isSignedIn.get()) {
    // Make sure you also do a check to see they're the right user.
    fetch('/servlet-name?idtoken=' + idToken);
  }
}
