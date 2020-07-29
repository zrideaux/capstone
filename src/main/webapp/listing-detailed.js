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
 * @param divCardContainerElement a div element where the listing will pop up
 * @param cardContainerElementDisplay the display of the card container
 * @param cardContainerElementId the id of the card container
 * @return a div with all the information pertaining to a listing
 */
export default function createListingDetailedView(divCardContainerElement, 
    cardContainerElementDisplay, cardContainerElementId) {

  const divCardElement = createDivElement( 
      '', 'card listing-detailed-card shadow-box', '');
  divCardContainerElement.appendChild(divCardElement);

  const divExitElement = createDivElement('', 'exit', '');
  
  // Make div exit keyboard accessible 
  //     (which includes tabs and enter keys)
  divExitElement.setAttribute("tabindex", "0");

  // when enter is pressed on this div, change the display to none and hide 
  //     this div
  divExitElement.addEventListener('click', function () {
    toggleDisplay(cardContainerElementDisplay, cardContainerElementId);
  });

  divExitElement.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      toggleDisplay(cardContainerElementDisplay, cardContainerElementId);
    }
  });

  divExitElement.appendChild(
    createIElement('close', 'material-icons', ''));
  divCardElement.appendChild(divExitElement);
  
  const divCardInfoElement = createDivElement(
      '', 'card-information-container', '');
  divCardElement.appendChild(divCardInfoElement);
  
  console.log('creating listing card information');
  divCardInfoElement.appendChild(createListingCardInformation());

  console.log('creating listing card description');
  divCardInfoElement.appendChild(createListingCardDescription());

  return divCardContainerElement;
}

/**
 * Create an element with listing details
 *
 * @return a div with the picture, name, category, reputation, listing details 
 *     (see below) and website of a listing.
 */
function createListingCardInformation() {
  const divCardInformation = createDivElement('', 'card-information', '');
  divCardInformation.appendChild(
      createImgElement('', 'picture of listing', 'card-picture', ''));
      
  divCardInformation.appendChild(
      createHElement('Los Angeles Food Bank', 1, 'card-name', ''));

  divCardInformation.appendChild(
      createHElement('Category', 2, 'detailed-attribute listing-tag pill', ''));      

  divCardInformation.appendChild(
      createHElement('Reputation: 200 up votes', 2, 'detailed-attribute pill ' 
      + 'reputation-pill', '')); 

  divCardInformation.appendChild(createListingDetails()); 

  divCardInformation.appendChild(
      createAElement('Website Link', '', '_blank', 'card-button', '')); 

  return divCardInformation;
}

/**
 * Create a div with listing details
 *
 * @return a div with the date the listing was made, the amount of views it has 
 *     received, its verifitcation, and contact info.
 */
function createListingDetails() {
  const divListingDetails = createDivElement('', 'listing-details', '');
  
  divListingDetails.appendChild(
    createPElement('Listing made on 01/02/20', 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement('1,234 Views', 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement('Verified or Community Reputation', 'listing-detail', ''));

  divListingDetails.appendChild(
    createPElement('Contact Info', 'listing-detail', ''));

  return divListingDetails
}

/**
 * Creates a div with listing descriptions
 *
 * @return a div with the description and comments of a listing
 */
function createListingCardDescription() {
  const divListingDetails = createDivElement('', 'card-description', '');

  divListingDetails.appendChild(
      createHElement('Description', '3', '', '')); 

  divListingDetails.appendChild(
    createPElement('Detailed description of what the event/fund/etc is for.', 
        '', ''));

  divListingDetails.appendChild(
      createHElement('How to help', '3', '', '')); 

  divListingDetails.appendChild(
    createPElement(
        'Detailed description of what the user can do to help the cause. Information about when/where the event is, if physical; links to relevant pages / donation links if digital. \
        Deliver or ship donations to Fake Address 123, Los Angeles, CA 34567 on weekdays. \
        Volunteer at Fake Address 123, Los Angeles, CA 34567 at 8:30PM everyday', 
        '', '')); 
  
  divListingDetails.appendChild(
      createHElement('Comments', '3', '', '')); 

  divListingDetails.appendChild(
    createPElement(
      'Users can write about experience with event/org/etc. Moderation might be touchy though.', '', ''));

  return divListingDetails;
}
