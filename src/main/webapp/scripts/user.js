import { 
  createAElement,
  createDivElement, 
  createHElement,
  createImgElement,
  createPElement
} from './htmlElement.js';

import { 
  toggleTabDisplay
} from './miscellaneous.js';

import { getListings } from './listing.js';

/**
 * Create an element that shows a listing detailed view
 *
 * @param divCardContainerElement a div element for the user's profile page
 * @return a div with all the information pertaining to a user
 */
export default function createUserProfile(divCardContainerElement) {
  const divCardInfoElement = createDivElement(
      '', 'card-information-container shadow-box', '');
  divCardContainerElement.appendChild(divCardInfoElement);
  
  // Creating User card information.
  const exBio = 'This is a fake bio!';
  const exEmail = 'abcde@gmail.com';
  const exName = 'Android Studios';
  divCardInfoElement.appendChild(createUserInformation(exBio, exEmail, exName));

  // Creating User card description.
  divCardInfoElement.appendChild(createUserListings());

  return divCardContainerElement;
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
      '');

  divCardInformation.appendChild(
      createImgElement('', 'profile picture', 'card-picture', ''));
      
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
 * @return a div with the description and comments of a listing.
 */
function createUserListings() {
  const divUserListings = createDivElement('', 'card-description ' + 
      'tab-listings-description', '');

  // Creating user's listing tabs.
  const listingsDisplay = 'block';
  const createdListingsId = 'created-listings';
  const upvotedListingsId = 'upvoted-listings';
  divUserListings.appendChild(createListingTabs(listingsDisplay, 
      createdListingsId, upvotedListingsId));

  const divUserListingContainer = createDivElement('', 'user-listing-container',
      '');
  divUserListings.appendChild(divUserListingContainer);

  const exListingKeys = [];
  const queryString = '/fetch-user-listings?listing-keys=';
  
  // Getting user's created listings.
  const queryStringCreatedListings = queryString + exListingKeys;
  getListings(divUserListingContainer, '', createdListingsId, 
      queryStringCreatedListings);

  // Getting user's upvoted listings.
  const upvotedListingsClass = 'upvoted-listings';
  const queryStringUpvotedListings = queryString + exListingKeys;
  getListings(divUserListingContainer, upvotedListingsClass, 
      upvotedListingsId, queryStringUpvotedListings);

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
