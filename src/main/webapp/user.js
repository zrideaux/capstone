import { 
  createAElement,
  createDivElement, 
  createHElement,
  createImgElement
  } from './htmlElement.js';

// User Page

/**
 * Create an element that shows a listing detailed view
 *
 * @param divCardContainerElement a div element for the user's profile page
 * @return a div with all the information pertaining to a user
 */
function createUserProfile(divCardContainerElement) {
  const divCardInfoElement = createDivElement(
      '', 'card-information-container shadow-box', '');
  divCardContainerElement.appendChild(divCardInfoElement);
  
  console.log('creating User card information');
  divCardInfoElement.appendChild(createUserInformation());

  console.log('creating User card description');
  divCardInfoElement.appendChild(createUserListings());

  return divCardContainerElement;
}  

/**
 * Create an element with User details
 *
 * @return a div with a picture, name, email, and form to create listing.
 */
function createUserInformation() {
  const divCardInformation = createDivElement('', 'card-information profile',
      '');

  divCardInformation.appendChild(
      createImgElement('', 'profile picture', 'card-picture', ''));
      
  divCardInformation.appendChild(
      createHElement('Android Studios', 1, 'user-name', ''));

  divCardInformation.appendChild(
      createHElement('abcde@gmail.com', 2, 'user-email', ''));      

  divCardInformation.appendChild(
      createAElement('Create listing', '', '', 'card-button create-listing', '')
      ); 

  return divCardInformation;
}

/**
 * Creates a div with user descriptions
 *
 * @return a div with the description and comments of a listing.
 */
function createUserListings() {
  const divUserListings = createDivElement('', 'card-description', '');

  console.log("Creating user's listing tabs");
  divUserListings.appendChild(createListingTabs());

  const divUserListingContainer = createDivElement('', 'user-listing-container',
      '');
  divUserListings.appendChild(divUserListingContainer);

  console.log("Creating user's created listings");
  divUserListingContainer.appendChild(createCreatedListings());

  console.log("Creating user's upvoted listings");
  divUserListingContainer.appendChild(createUpvotedListings());

  return divUserListings;
}

/**
 * Creates a div with listing tabs.
 *
 * @return a div with two tabs one for a user's created listings and another 
 *     for their upvoted listings.
 */
function createListingTabs() {
  const divTabs = createDivElement('', 'tabs', '');

  const aCreatedListings = createAElement('', '#created-listings', '', 
      'tab created-listings', '');
  aCreatedListings.appendChild(
      createHElement('Created Listings', '3', '', ''));
  divTabs.appendChild(aCreatedListings);

  const aUpvotedListings = createAElement('', '#upvoted-listings', '', 
      'tab upvoted-listings', '');
  aUpvotedListings.appendChild(
      createHElement('Upvoted Listings', '3', '', ''));  
  divTabs.appendChild(aUpvotedListings);

  return divTabs;
}

/**
 * Creates a div with a user's created listings.
 *
 * @return a div with all of a user's created listings.
 */
function createCreatedListings() {
  const divCreatedListings = createDivElement('', '', 'created-listings');

  return divCreatedListings;
}

/**
 * Creates a div with a user's upvoted listings.
 *
 * @return a div with all of a user's upvoted listings.
 */
function createUpvotedListings() {
  const divUpvotedListings = createDivElement('', '', 'upvoted-listings');

  return divUpvotedListings;
}

export { createUserProfile };
