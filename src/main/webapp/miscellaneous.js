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
 * Toggles the display of this tab Element, and make the display of the other 
 *     tab element none. 
 * 
 * @param display the display of the element
 * @param id used to get an element with this id
 * @param otherId used to get the other tab element with this id
 */
function toggleTabDisplay(display, id, otherId) {
    toggleDisplay(display, id);
    let otherElement = document.getElementById(otherId);
    otherElement.style.display = 'none';
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
 * Hide all dropdown menus when called.
 */
function hideDropdownMenus() {
  const allDropdowns = document.getElementsByClassName('dropdown-menu');
  for (let i = 0; i < allDropdowns.length; i ++) {
    allDropdowns[i].style.display = 'none';
  }

}
