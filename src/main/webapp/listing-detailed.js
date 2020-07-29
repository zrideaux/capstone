import { 
  createAElement,
  createDivElement, 
  createHElement,
  createIElement,  
  createImgElement, 
  createPElement 
} from './htmlElement.js';

// miscellaneous functions

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

// Listing Page

/**
 * Create an element that shows a listing detailed view
 *
 * @param divCardContainerElement a div element where the listing will pop up
 * @param cardContainerElementDisplay the display of the card container
 * @param cardContainerElementId the id of the card container
 * @return a div with all the information pertaining to a listing
 */
function createListingDetailedView(divCardContainerElement, 
    cardContainerElementDisplay, cardContainerElementId) {

  const divCardElement = createDivElement( 
      '', 'card listing-detailed-card shadow-box', '');
  divCardContainerElement.appendChild(divCardElement);

  const divExitElement = createDivElement(
      'toggleDisplay("'+ cardContainerElementDisplay + '", "' + 
      cardContainerElementId + '")', 'exit', '');
  
  // Make div exit keyboard accessible 
  //     (which includes tabs and enter keys)
  divExitElement.setAttribute("tabindex", "0");

  // when enter is pressed on this div, change the display to none and hide 
  //     this div
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
      createHElement('Category', 2, 'category-pill listing-tag', ''));      

  divCardInformation.appendChild(
      createHElement('Reputation: 200 up votes', 2, 'reputation-pill', '')); 

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
    createPElement('Listing made on 01/02/20', '', ''));

  divListingDetails.appendChild(
    createPElement('1,234 Views', '', ''));

  divListingDetails.appendChild(
    createPElement('Verified or Community Reputation', '', ''));

  divListingDetails.appendChild(
    createPElement('Contact Info', '', ''));

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

export { toggleDisplay, toggleTabDisplay, createListingDetailedView };
