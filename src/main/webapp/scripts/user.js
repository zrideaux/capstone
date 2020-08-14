import { authenticate } from './authentication.js';

import {
  createScrollToTopButton,
  scrollToTop
} from './go-to-top-button.js';

import { 
  createAElement,
  createDivElement, 
  createHElement,
  createImgElement,
  createPElement
} from './htmlElement.js';

import { createListings } from './listing.js';

import { 
  displayErrorMessage,
  isErrorMessage, 
  keyboardAccessibleOnClick,
  toggleTabDisplay
} from './miscellaneous.js';

/**
 * When the page loads check if the user if logged in, and if so create a user's
 *     profile page.
 */
window.onload = function() {
  authenticate(getUserProfile);
  const header = document.getElementById('nav-box');
  header.style.cursor ='pointer';
  keyboardAccessibleOnClick(header, scrollToTop, scrollToTop);
}

/**
 * Retrieves a User and creates a profile for them
 */
export default function getUserProfile() {
  const divCardContainerElement = document.getElementById("user");

  fetch('/fetch-user')
      .then(response => response.json())
      .then((user) => {
        if(isErrorMessage(user)) {
          displayErrorMessage(user);
        } else {
          divCardContainerElement.appendChild(createUserProfile(user));
          addBackToTopButton();
        }
      })
}

/**
 * Create an element that shows a listing detailed view
 *
 * @param user JSON that represents a user
 * @return a div with all the information pertaining to a user
 */
function createUserProfile(user) {
  const divCardInfoElement = createDivElement(
      '', 'card-information-container shadow-box', '');
  
  // Creating User card information.
  const userInformationDiv = createUserInformation(user.bio, user.email, 
      user.username);
  divCardInfoElement.appendChild(userInformationDiv);

  // Creating User card description.
  divCardInfoElement.appendChild(createUserListings(user.createdListings, 
      user.upvotedListings, userInformationDiv)); 

  return divCardInfoElement;
}  

/**
 * Create an element with User details
 *
 * @param bio the user's bio
 * @param email the email of the user
 * @param name the name of the user
 * @return a div with a picture, name, email, and form to create listing.
 */
function createUserInformation(bio, email, name) {
  const divCardInformation = createDivElement('', 'card-information profile',
      'user-info');

  divCardInformation.appendChild(
      createImgElement('', 'profile picture', 'card-picture', ''));
      
  console.log("NAME: " + name);
  divCardInformation.appendChild(
      createHElement(name, 1, 'user-name', ''));

  divCardInformation.appendChild(
      createHElement(email, 2, 'user-email', ''));    

  divCardInformation.appendChild(
      createPElement(bio, 'user-bio', ''));

  divCardInformation.appendChild(
      createAElement('Create listing', 'newlisting.html', '', 'card-button', '')
      );        

  return divCardInformation;
}

/**
 * Creates a div with user descriptions
 *
 * @param createdListings An array that contains JSON that represents a User's 
 *     creates Listings.
 * @param upvotedListings An array that contains JSON that represents a User's 
 *     upvoted Listings.
 * @return a div with the description and comments of a listing.
 */
function createUserListings(createdListings, upvotedListings, userInformationContainer) {
  const divUserListings = createDivElement('', 'card-description ' + 
      'tab-listings-description', '');

  // Creating user's listing tabs.
  const listingsDisplay = 'block';
  const createdListingsId = 'created-listings';
  const upvotedListingsId = 'upvoted-listings';
  divUserListings.appendChild(createListingTabs(listingsDisplay, 
      createdListingsId, upvotedListingsId));

  const divUserListingContainer = createDivElement('', 'user-listing-container',
      'user-listing-container');
  divUserListings.appendChild(divUserListingContainer);
  
  // Create user's created listings.
  divUserListingContainer.appendChild(
      createListings(createdListings, '', createdListingsId));

  // Create user's upvoted listings.
  const upvotedListingsClass = 'upvoted-listings';
  divUserListingContainer.appendChild(
      createListings(upvotedListings, upvotedListingsClass, 
          upvotedListingsId));       

  return divUserListings;
}

/**
 * Creates a div with listing tabs.
 *
 * @return a div with two tabs one for a user's created listings and another 
 *     for their upvoted listings.
 */
function createListingTabs(listingsDisplay, createdListingsId, 
    upvotedListingsId) {
  const divTabs = createDivElement('', 'tabs', '');
  
  // Create tabs.
  const createdListingsTabId = "created-listings-tab";
  const upvotedListingsTabId = "upvoted-listings-tab";
  const createdListingsTabClass = "created-listings-tab";
  const upvotedListingsTabClass = "upvoted-listings-tab";

  // Create Created Listings tab.
  divTabs.appendChild(createTab(
    listingsDisplay, createdListingsId, upvotedListingsId, '3', 
    upvotedListingsTabId, createdListingsTabClass, createdListingsTabId, 
    'Created Listings'));
    
  // Create Upvoted Listings tab.
  divTabs.appendChild(createTab(
    listingsDisplay, upvotedListingsId, createdListingsId, '3', 
    createdListingsTabId, upvotedListingsTabClass, upvotedListingsTabId, 
    'Upvoted Listings'));

  return divTabs;
}

/**
 * Creates a div that represents tab.
 *
 * @param elementDisplay the display of the element associated with this tab
 * @param elementId the id of the element associated with this tab
 * @param hNum the number for the heading (ex: h1 ,h2, h3)
 * @param tabName the name of this name that is displayed to the user
 * @return a div that represents a tab.
 */
function createTab(elementDisplay, elementId, elementOtherId, hNum, otherTabId,
    tabClass, tabId, tabName) {
  // Create <h> element that represents a tab button.
  const hTab = createHElement(tabName, hNum, 'tab pill ' + tabClass, tabId);

  hTab.setAttribute("tabindex", "0");

  // When enter is pressed on this div, change the display to elementDisplay.
  hTab.addEventListener("click", function(){ 
    toggleTabDisplay(elementDisplay, elementId, elementOtherId, otherTabId,   
        tabId) 
  });

  hTab.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      toggleTabDisplay(elementDisplay, elementId, elementOtherId, otherTabId, 
          tabId);
    }
  });
  
  return hTab;
}

/**
 * Add a Back to top button that appears in the user info when the user scrolls 
 *     past the listing container.
 */
function addBackToTopButton() {
  // 
  const userInformationContainer = document.getElementById('user-info');
  const positionFromTop = document.getElementById('user-listing-container')
      .getBoundingClientRect().top;
  userInformationContainer.appendChild(
      createScrollToTopButton('user-go-to-top-button', positionFromTop));
}
