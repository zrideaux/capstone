import { authenticate } from './authentication.js';

import {
  addBackToTopButtonBefore,
  makeNavBarScrollToTop
} from './scroll-to-top.js';

import { 
  getListings,
  getListingsResponseJson 
} from './listing.js';

import addLoadingSpinner from './loading-spinner.js';

import {
  checkAllCheckboxes,
  getCheckboxesByName,  
  getRadioByName,
  keyboardAccessible,
  keyboardAccessibleOnClick,
  mapElementsByName
} from './miscellaneous.js';

import {
  getCall,
  initiateLastCall,
  isLatestCall
} from './tracking-response.js';

const loaderId = 'search-loader';
let location = '';

/**
 * When the search page loads, add on click functions to input tags and see all 
 *     span tag.
 */
window.onload = function() {
  authenticate();
  addBackToTopButtonBefore('back-to-top-search', 'listings', 
      'main-search');
  makeNavBarScrollToTop();
  initiateLastCall();
  addOnclickToInputs();
  getCurrentPosition();
}

/**
 * Displays listings based on the search parameters (includes type filters, 
 *     radius filter, and sortBy).
 */
function displayListings() {
  const containerElement = document.getElementById("listings");

  containerElement.innerHTML = '';
  // Add loader
  addLoadingSpinner(containerElement, loaderId);
  const queryString = '/fetch-listings?' + getSearchParameters();
  getListings(containerElement, '', 'search-listings', 
      queryString, displayListingsResponseJson);
}

/**
 * Checks if the trackingResponse is from the latest rpc. 
 * If it is, then create listings from the rpc.
 * Made to be used in the getListings function.
 *
 * @param containerElement the element that contains listings.
 * @param trackingResponse a JSON that represents a TrackingResponse which 
 *     contains a call number and a JSON that represents a listing.
 * @param listingsClass the class attribute for the listings div.
 * @param listingsId the id attribute for the listings div.
 */
function displayListingsResponseJson(containerElement, trackingResponse, 
    listingsClass, listingsId) {
  if (isLatestCall(trackingResponse.call)) {
    // Remove loader
    containerElement.innerHTML = '';
    
    //Holds fetchListingsData Object
    const fetchListingsData = trackingResponse.response;
    
    //Displays users location
    if (fetchListingsData.userLocation !== '') {
      document.getElementById('search-location-input').value = fetchListingsData.userLocation;
    }
    
    // Show listings
    getListingsResponseJson(containerElement, fetchListingsData.listings,  
        listingsClass, listingsId);
  }
}

/**
 * Add on click and when submit key is pressed to checkbox/radio inputs and to 
 *     see all span element.
 */
function addOnclickToInputs() {
  // Add onclick and onsubmit function to see all span element.
  const seeAllElement = document.getElementById('see-all');
  keyboardAccessible(seeAllElement, displayListingsShowAllFilters, 
      displayListingsShowAllFilters, "0");
 
  // Checkbox is tab accessible.
  // Add onclick and onsubmit function to filter checkbox inputs.
  mapElementsByName(displayListingsOnClickCheckbox, 'search-type-option');

  // Radio is not tab accessible.
  const displayListingsOnClickRadio = (radio) => {
    keyboardAccessibleOnClick(radio, getCurrentPosition, getCurrentPosition);
  }

  // Add onclick and onsubmit function to radius radio inputs.
  mapElementsByName(displayListingsOnClickRadio, 'search-radius-option');

  // Add onclick and onsubmit function to sort radio inputs.
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
    getCurrentPosition();
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
  getCurrentPosition();
}

/**Get's current positsion of user based on html5 browser location */
function getCurrentPosition() {
  navigator.geolocation.getCurrentPosition(showResult, geolocationError); 
}

/** Upon access location is equal to the user's latitude and longitude */
function showResult(results) {
  location = results.coords.latitude + ' ' + results.coords.longitude;
  displayListings();
}

/**Upon denial location is equal to the users input */
function geolocationError(){
  location = document.getElementById('search-location-input').value;
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
  let param = 'type-filters=' + filterTypes;
  param += '&radius-filter=' + filterRadius;
  param += '&sortBy=' + sort;
  param += '&location=' + location;
  param += '&call=' + getCall();
  return param;
}
