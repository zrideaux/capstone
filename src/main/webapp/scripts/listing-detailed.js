import { 
  createAElement,
  createButtonElement,
  createDivElement, 
  createHElement,
  createIElement,  
  createImgElement, 
  createPElement, 
  createSpanElement
} from './htmlElement.js';

import { toggleDisplay } from './miscellaneous.js';

import { 
  setInitialVoteState,
  voteClicked
} from './reputation.js';

/**
 * Create an element that shows a listing detailed view
 *
 * @param listing JSON that represents a listing and has listing information
 * @param cardContainerElementDisplay the display of the card container
 * @param cardContainerElementId the id of the card container
 * @return a div with all the information pertaining to a listing
 */
export default function createListingDetailedView(listing, 
    cardContainerElementDisplay, cardContainerElementId) {
  const divCardContainerElement = createDivElement('', 'card-container modal',
      cardContainerElementId);

  const divCardElement = createDivElement( 
      '', 'card listing-detailed-card shadow-box', '');
  divCardContainerElement.appendChild(divCardElement);
  
  divCardElement.appendChild(createExitElement(
      cardContainerElementDisplay, cardContainerElementId));
  
  const divCardInfoElement = createDivElement(
      '', 'card-information-container', '');
  divCardElement.appendChild(divCardInfoElement);
  
  // Creating listing card information
  const type = listing.type;
  const dateCreated = listing.dateCreated;
  const downvotes = listing.downvotes.toLocaleString();
  const imageURL = listing.imageURL;
  const key = listing.key;
  const name = listing.name;
  const upvotes = listing.upvotes.toLocaleString();
  const vote = listing.vote
  const views = listing.views.toLocaleString();
  const website = listing.website;  
  divCardInfoElement.appendChild(createListingCardInformation(type, dateCreated,
      downvotes, imageURL, key, name, upvotes, views, vote, website));

  // Creating listing card description
  const comments = '';
  const description = listing.description;
  const howToHelp = listing.howToHelp;
  divCardInfoElement.appendChild(createListingCardDescription(comments, 
      description, howToHelp));

  return divCardContainerElement;
}

/**
 * Create an element that represents an exit button for the user to exit the 
 *     current "page"
 * 
 * @param elementDisplay the display of the element this function is modifying
 * @param elementId the id of the element this function is modifying
 * @return an element that represents an exit button
 */
function createExitElement(elementDisplay, elementId) {
  const divExitElement = createDivElement('', 'exit', '');
  
  // Make div exit keyboard accessible 
  //     (which includes tabs and enter keys)
  divExitElement.setAttribute("tabindex", "0");

  // when enter is pressed on this div, change the display to none and hide 
  //     this div
  divExitElement.addEventListener('click', function () {
    toggleDisplay(elementDisplay, elementId);
  });

  divExitElement.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      toggleDisplay(elementDisplay, elementId);
    }
  });

  divExitElement.appendChild(
    createIElement('close', 'material-icons', ''));

  return divExitElement;
}

/**
 * Create an element with listing details
 *
 * @param type the type of this listing
 * @param dateCreated the date this listing was created
 * @param imageURL the source of the listing image
 * @param name the name of this listing
 * @param upvotes the number of upvotes this listing has
 * @param views the number of views this listing has received
 * @param websiteLink the link to this listings website
 * @return a div with the picture, name, type, reputation, listing details 
 *     (see below) and website of a listing.
 */
function createListingCardInformation(type, dateCreated, downvotes, imageURL,
    key, name, upvotes, views, vote, websiteLink) {
  const divCardInformation = createDivElement('', 'card-information', '');
  divCardInformation.appendChild(
      createImgElement(imageURL, 'picture of listing', 'card-picture', ''));
      
  divCardInformation.appendChild(
      createHElement(name, 1, 'card-name', ''));

  divCardInformation.appendChild(
      createHElement(type, 2, 'detailed-attribute listing-tag pill', ''));      

  divCardInformation.appendChild(createHElement('Reputation', '2', 'reputation-heading'));
  divCardInformation.appendChild(
      createReputationContainer(downvotes, key, upvotes, vote));

  divCardInformation.appendChild(createListingDetails(dateCreated, views)); 

  if (websiteLink.length > 0) {
    divCardInformation.appendChild(
        createAElement('Website Link', websiteLink, '_blank', 'card-button', ''));  
  }

  return divCardInformation;
}

/**
 * Create a div with listing details
 *
 * @param dateCreated the date this listing was created
 * @param views the number of views this listing has received 
 * @return a div with the date the listing was made, the amount of views it has 
 *     received, its verifitcation, and contact info.
 */
function createListingDetails(dateCreated, views) {
  const divListingDetails = createDivElement('', 'listing-details', '');
  
  divListingDetails.appendChild(
    createPElement('Listing made on ' + dateCreated, 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement(views + ' Views', 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement('Contact Info', 'listing-detail', ''));

  return divListingDetails
}

/**
 * Creates a div with listing descriptions
 *
 * @param comments the comments this listing has received
 * @param description the description of this listing
 * @param howToHelp the text that descripes how to help this cause/listing
 * @return a div with the description and comments of a listing
 */
function createListingCardDescription(comments, description, howToHelp) {
  const divListingDetails = createDivElement('', 'card-description', '');

  divListingDetails.appendChild(
      createHElement('Description', '3', '', '')); 

  divListingDetails.appendChild(
    createPElement(description, '', ''));

  divListingDetails.appendChild(
      createHElement('How to help', '3', '', '')); 

  divListingDetails.appendChild(

    createPElement(howToHelp,'', '')); 
  
  divListingDetails.appendChild(
      createHElement('Comments', '3', '', '')); 

  divListingDetails.appendChild(

    createPElement(comments, '', ''));

  return divListingDetails;
}

/**
 * Creates a container that holds uniquely id'd downvote and upvote button
 * for a listing.
 *
 * @param downvotes an int representing the number of downvotes a listing has
 * @param key a string that is the key of a listing
 * @param upvotes an int representing the number of upvotes a listing has
 * @param existingVote the vote which the current user already has on a listing
 */
function createReputationContainer(downvotes, key, upvotes, existingVote) {
  let reputationContainer = createDivElement('', '', 'reputation-button-container');
  
  // Create upvote button
  let upvoteButton = createButtonElement('', 'reputation-button',
      'reputation-upvote-' + key);
  let upvoteIcon = createIElement('thumb_up', 'material-icons', '');
  let upvoteCount = createSpanElement(upvotes, 'reputation-count', 'reputation-count-upvotes-' + key);
  upvoteButton.appendChild(upvoteIcon);
  upvoteButton.appendChild(upvoteCount);
  
  // Create downvote button
  let downvoteButton = createButtonElement('', 'reputation-button',
      'reputation-downvote-' + key);
  let downvoteIcon = createIElement('thumb_down', 'material-icons', '');
  let downvoteCount = createSpanElement(downvotes, 'reputation-count', 'reputation-count-downvotes-' + key);
  downvoteButton.appendChild(downvoteIcon);
  downvoteButton.appendChild(downvoteCount);

  // Set the initial state of the buttons when they're made
  setInitialVoteState(downvoteButton, upvoteButton, existingVote, key);

  reputationContainer.appendChild(upvoteButton);
  reputationContainer.appendChild(downvoteButton);

  return reputationContainer;
}
