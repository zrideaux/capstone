/**
 * Hide all dropdown menus when called.
 */
function hideDropdownMenus() {
  const allDropdowns = document.getElementsByClassName('dropdown-menu');
  for (let i = 0; i < allDropdowns.length; i ++) {
    allDropdowns[i].style.display = 'none';
  }
}

/**
 * If the string is an error message display the error message as an alert
 *
 * @param str A String that might be an error message
 */
function ifErrorDisplayMessage(str) {
  if (isErrorMessage(str)) {
    displayErrorMessage(str);
  }
}

/** 
 * Returns a boolean stating whether a String is an error message or not
 *
 * @param str a string that will be checked to see if it is an error message
 * @return boolean stating whether or not it is an error message
 */
function isErrorMessage(str) {
  const errorIntro = "Servlet Error:";
  const errorIntroLength = errorIntro.length;
  const isString = typeof str === "string";
  const isLength = str.length > errorIntroLength;
  let isSubstring = false;
  if (isLength) {
    // the error message (str) comes with quotes
    isSubstring = str.startsWith(errorIntro);
  }
  console.log("String: " + str);
  console.log("String length: " + str.length);
  console.log(isString + " " + isLength + " " + isSubstring);
  return (isString && isLength && isSubstring);
}

/** 
 * Creates an alert with the errorMessage
 *
 * @param errorMessage the error message to display to the user
 */
function displayErrorMessage(errorMessage) {
  console.log(errorMessage);
  window.alert(errorMessage);
}

/** 
 * Toggles the display of an element. 
 * 
 * @param display the display of the element
 * @param id used to get an element with this id
 */
function toggleDisplay(display, id) {
  let element = document.getElementById(id);
  const elementStyle = getComputedStyle(element, null).display;
  if (elementStyle === display) {
    console.log('Element with id ' + id + ' is now hidden.')
    console.log('Display: ' + elementStyle + ' is now ' + display);
    element.style.display = 'none';
  } else {
    console.log('Element with id ' + id + ' is now visible.')
    console.log('Display: ' + elementStyle + ' is now ' + display);
    element.style.display = display;
  }  
}

/**
 * Toggle a specified dropdown menu when called. 
 *
 * @param menuName text that specifies the id of the menu to be toggled.
 */
function toggleDropdown(menuName) {
  const dropdownMenu = document.getElementById(menuName);
  let menuIsClosed = (window.getComputedStyle(dropdownMenu).display === 'none');

  // Hide any open dropdown menus.
  hideDropdownMenus();
  
  // Display specified menu if it wasn't already open
  if (menuIsClosed) {
    dropdownMenu.style.display = 'block';      
  }
}

/** 
 * Toggles the display of this tab Element, and make the display of the other 
 *     tab element none. 
 * Also changes the background of this tab to #8EEEDE and the other tab to 
 *     inherit
 * 
 * @param elementDisplay the display of the element
 * @param elementId used to get an element with this id
 * @param otherElementId used to get the other tab element with this id
 * @param otherTabId used to change the background of the other tab to inherit
 * @param tabId used to change the background of this tab to #8EEEDE
 */
function toggleTabDisplay(elementDisplay, elementId, otherElementId,    
    otherTabId, tabId) {
  toggleDisplay(elementDisplay, elementId);
  let tab = document.getElementById(tabId);
  tab.style.background = '#8EEEDE';

  let otherListing = document.getElementById(otherElementId);
  otherListing.style.display = 'none';
  let otherTab = document.getElementById(otherTabId);
  otherTab.style.background = 'inherit';
}

export { 
  hideDropdownMenus, 
  ifErrorDisplayMessage,
  isErrorMessage, 
  displayErrorMessage,
  toggleDisplay, 
  toggleDropdown, 
  toggleTabDisplay 
  };
