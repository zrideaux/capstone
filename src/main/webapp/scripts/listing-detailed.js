import { 
  createAElement,
  createDivElement, 
  createHElement,
  createIElement,  
  createImgElement, 
  createPElement 
} from './htmlElement.js';

import { toggleDisplay } from './miscellaneous.js';

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
  
  console.log('creating listing card information');
  const exCategory = 'Category';
  const exDate = '01/02/20';
  const exImgSrc = '';
  const exName = 'Los Angeles Food Bank';
  const exUpvotes = 205;
  const exViews = '1,234';
  const exWebsite = '';
  divCardInfoElement.appendChild(createListingCardInformation(exCategory, 
      exDate, exImgSrc, exName, exUpvotes, exViews, exWebsite));

  console.log('creating listing card description');
  const exComments = 'Users can write about experience with event/org/etc. ' +
      'Moderation might be touchy though.';
  const exDescription = 'Detailed description of what the event/fund/etc is '
      + 'for.';
  const exHowToHelp = 'Detailed description of what the user can do to help ' + 
      'the cause. Information about when/where the event is, if physical; ' + 
      'links to relevant pages / donation links if digital. Deliver or ship ' + 
      'donations to Fake Address 123, Los Angeles, CA 34567 on weekdays. ' + 
      'Volunteer at Fake Address 123, Los Angeles, CA 34567 at 8:30PM everyday';
  divCardInfoElement.appendChild(createListingCardDescription(exComments, 
      exDescription, exHowToHelp));

  return divCardContainerElement;
}

/**
 * Create an element that represenets an exit button for the user to exit the 
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
 * @param category the category of this listing
 * @param date the date this listing was created
 * @param imgSrc the source of the listing image
 * @param name the name of this listing
 * @param upvotes the number of upvotes this listing has
 * @param views the number of views this listing has received
 * @param websiteLink the link to this listings website
 * @return a div with the picture, name, category, reputation, listing details 
 *     (see below) and website of a listing.
 */
function createListingCardInformation(category, date, imgSrc, name, upvotes, 
    views, websiteLink) {
  const divCardInformation = createDivElement('', 'card-information', '');
  divCardInformation.appendChild(
      createImgElement(imgSrc, 'picture of listing', 'card-picture', ''));
      
  divCardInformation.appendChild(
      createHElement(name, 1, 'card-name', ''));

  divCardInformation.appendChild(
      createHElement(category, 2, 'detailed-attribute listing-tag pill', ''));      

  divCardInformation.appendChild(
      createHElement('Reputation: ' + upvotes + ' upvotes', 2, 
      'detailed-attribute pill reputation-pill', '')); 

  divCardInformation.appendChild(createListingDetails(date, views)); 

  divCardInformation.appendChild(
      createAElement('Website Link', websiteLink, '_blank', 'card-button', ''));

  return divCardInformation;
}

/**
 * Create a div with listing details
 *
 * @param name the name of this listing
 * @param views the number of views this listing has received 
 * @return a div with the date the listing was made, the amount of views it has 
 *     received, its verifitcation, and contact info.
 */
function createListingDetails(date, views) {
  const divListingDetails = createDivElement('', 'listing-details', '');
  
  divListingDetails.appendChild(
    createPElement('Listing made on ' + date, 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement(views + ' Views', 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement('Verified or Community Reputation', 'listing-detail', ''));

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
