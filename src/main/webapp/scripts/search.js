import { authenticate } from './authentication.js';

import { getListings } from './listing.js';

import { 	
  checkAllCheckboxes,
  getCheckboxesByName,	
  getRadioByName,
  keyboardAccessible,
  keyboardAccessibleOnClick,
  mapElementsByName
} from './miscellaneous.js';	

/**
 * When the search page loads, add on click functions to input tags and see all 
 *     span tag.
 */
window.onload = function() {
  authenticate(addOnclickToInputs);
}

/**
 * Displays listings based on the search parameters (includes type filters, 
 *     radius filter, and sortBy).
 */
export default function displayListings() {
  const containerElement = document.getElementById("listings");
  containerElement.innerHTML = '';
  const queryString = '/fetch-listings?' + getSearchParameters();
  getListings(containerElement, '', 'search-listings', queryString);
}

/**
 * 
 */
function addOnclickToInputs() {
  const seeAllElement = document.getElementById('see-all');
  keyboardAccessible(seeAllElement, displayListingsShowAllFilters, 
      displayListingsShowAllFilters, "0");
 
  // Checkbox is tab accessible
  mapElementsByName(displayListingsOnClickCheckbox, 'search-type-option');

  // Radio is not tab accessible
  const displayListingsOnClickRadio = (radio) => {
    keyboardAccessibleOnClick(radio, displayListings, displayListings);
  }

  mapElementsByName(displayListingsOnClickRadio, 'search-radius-option');
  mapElementsByName(displayListingsOnClickRadio, 'search-sort-option');
}

/**
 * Add onclick and when enter key is pressed event listeners to the checkbox 
 *     element.
 * This function is used to be passed as a parameter to the mapElementsByName 
 *     func, and can only have one parameter.
 *
 * @param checkbox the checkbox element that will be given event listeners
 */
function displayListingsOnClickCheckbox(checkbox) {
  // On enter key, uncheck/check the checkbox and fetch listings
  const changeCheckedAndDisplayListings = () => {
    checkbox.checked = !checkbox.checked;
    displayListings();
  }

  keyboardAccessibleOnClick(checkbox, displayListings, 
      changeCheckedAndDisplayListings);
};

/**
 * Display listings based on the search parameters and make all filter 
 *     checkboxes checked.
 */
function displayListingsShowAllFilters() {
  checkAllCheckboxes('search-type-option');
  displayListings();
}

/**
 * Get the search parameters (type filters, radius filter, and sortBy) from the 
 *     search page.
 *
 * @return a String that represents a query parameter
 */
function getSearchParameters() {
  const filterTypes = getCheckboxesByName('search-type-option');
  const filterRadius = getRadioByName('search-radius-option');
  const sort = getRadioByName('search-sort-option');
  const location = document.getElementById('search-location-input').value;
  let param = 'type-filters=' + filterTypes;
  param += '&radius-filter=' + filterRadius;
  param += '&sortBy=' + sort;
  param += '&location=' + location;
  return param;
}
