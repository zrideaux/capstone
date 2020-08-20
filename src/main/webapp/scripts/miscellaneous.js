/**	
 * Gets the checkbox and make all of them checked.
 * 	
 * @param name the name of the checkbox group	
 */	
function checkAllCheckboxes(name) {	
  let checkCheckbox = (checkbox) => {
    checkbox.checked = true;
  }
  
  mapElementsByName(checkCheckbox, name);
}	

/**	
 * Gets the checkbox value(s) that have been checked or '' if none have been 	
 *     checked.
 * 	
 * @param name the name of the checkbox group	
 * @return the values of the checkbox that are checked or '' if none are 
 *     checked	.
 */	
function getCheckboxesByName(name) {	
  const checkboxGroup = document.getElementsByName(name);	
  let checkedCheckboxes = '';	
  for (let i = 0; i < checkboxGroup.length; i++) {	
    const checkbox = checkboxGroup[i];	
    if (checkbox.checked) {	
      checkedCheckboxes += checkbox.value + '@';	
    }	
  }	

  // Remove the last @ and return the String	
  return checkedCheckboxes.substring(0, checkedCheckboxes.length - 1);	
}	

/**	
 * Gets the radio value that has been checked or '' if none have been checked.
 * 	
 * @param name the name of the radio group	
 * @return the value of the radio that is checked or '' if none are checked.
 */	
function getRadioByName(name) {	
  const radioGroup = document.getElementsByName(name);	
  for (let i = 0; i < radioGroup.length; i++) {	
    const radio = radioGroup[i];	
    if (radio.checked) {	
      return radio.value;	
    }	
  }	
  return '';	
}

/**
 * Gets the parameters (key and value) from the url.
 *
 * @return an object
 */
function getUrlParams() {
  var url = document.location.href,
      data = {}, 
      datum,
      queryString = url.split('?');
  console.log("URL: " + url);
  if (queryString.length > 1) {
    const params = queryString[1].split('&');
    console.log("QUERY string: " + queryString[1]);
    for (var i = 0, l = params.length; i < l; i++) {
      console.log("PARAMS: " + params[i]);
      datum = params[i].split('=');
      console.log("key: " + datum[0] + ", value: " + datum[1]);
      data[datum[0]] = datum[1];
    }  
  }

  return data;
}

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
  if (isString && isLength) {
    // the error message (str) comes with quotes
    isSubstring = str.startsWith(errorIntro);
  }
  return (isString && isLength && isSubstring);
}

/** 
 * Returns a boolean stating whether a String is a success message or not
 *
 * @param str a string that will be checked to see if it is a success message
 * @return boolean stating whether or not it is a success message
 */
function isSuccessMessage(str) {
  return str === "Servlet Success";
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
 * Makes element keyboard accessible (tabs and enter key) and calls a function 
 *     when "clicked" on.
 *
 * @param element the element to add a tabIndex and 
 * @param onclickFunc the function to execute with this element is "clicked" on
 * @param tabindex the tabindex of this element
 */
function keyboardAccessible(element, onclickFunc, onenterFunc, tabindex = '0') {
  element.setAttribute("tabindex", tabindex);
  keyboardAccessibleOnClick(element, onclickFunc, onenterFunc);
}

/**
 * Makes element keyboard accessible and calls a function when "clicked" on
 *
 * @param element the element to add a tabIndex and 
 * @param onclickFunc the function to execute with this element is "clicked" on
 */
function keyboardAccessibleOnClick(element, onclickFunc, onenterFunc = onclickFunc) {
  // when enter is pressed on this div, change the display to none and hide 
  //     this div
  element.addEventListener('click', onclickFunc);

  element.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      onenterFunc();
    }
  });
}

/**
 * Get elements by name and apply a function to these elements
 *
 * @func the function to apply to the elements
 * @name the name of the elements group
 */
function mapElementsByName(func, name) {
  const elementGroup = document.getElementsByName(name);	
  for (let i = 0; i < elementGroup.length; i++) {	
    func(elementGroup[i]);
  }	
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
  checkAllCheckboxes,
  displayErrorMessage,
  getCheckboxesByName,	
  getRadioByName,
  getUrlParams,
  hideDropdownMenus, 
  ifErrorDisplayMessage,
  isErrorMessage, 
  isSuccessMessage,
  keyboardAccessible,
  keyboardAccessibleOnClick,
  mapElementsByName,
  toggleDisplay, 
  toggleDropdown, 
  toggleTabDisplay 
  };
