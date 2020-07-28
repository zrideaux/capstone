
/**
 * Create a link for users to log in or out of their account.
 */
function createLogInOutLink() {
  fetch('/authentication').then(response => response.json()).then(authenticationInfo => {
    let link = document.getElementById('login-link');

    if (authenticationInfo.loggedIn === 'true') {
      link.href = authenticationInfo.logoutUrl;
      link.innerText = 'Log Out';
    } else {
      link.href = authenticationInfo.loginUrl;
      link.innerText = 'Log In';
    }
  });
}

/**
 * Send a request to create a new user. Log a response message.
 */
function createNewUser() {
  userName = 'Fake Username';
  userBio = 'this is a fake bio!';
  
  const createUserUrl = '/create-user?name=' + userName + '&bio=' + userBio;

  fetch(createUserUrl, {method: 'POST'}).then(response => response.text()).then(message => {
    console.log(message);
  });
}
