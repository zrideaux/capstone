// Functions that create html elements

/** 
 * Creates a <div> element containing class and id attribute. 
 *
 * @param onclickAttribute the js that is executed when this button is clicked
 * @param classAttribute the name of the class of this div element
 * @param idAttribute the name of the id of this div element
 * @return returns a div element with a class and id
 */
function createDivElement(onclickAttribute, classAttribute, idAttribute) {
  const divElement = document.createElement('div');
  divElement.setAttribute('onclick', onclickAttribute);
  divElement.setAttribute('class', classAttribute);
  divElement.setAttribute('id', idAttribute);
  return divElement;
}

/** 
 * Creates an <i> element containing text, class and id attribute. 
 *
 * @param text the text that will be displayed 
 * @param classAttribute the name of the class of this i element
 * @param idAttribute the name of the id of this i element
 * @return returns an i element with text, class, and id
 */
function createIElement(text, classAttribute, idAttribute) {
  const iElement = document.createElement('i');
  iElement.setAttribute('class', classAttribute);
  iElement.setAttribute('id', idAttribute);
  iElement.innerText = text;
  return iElement;
}

/** 
 * Creates a <h> element with rank and containing text. 
 * 
 * @param text the text that will be displayed
 * @param rank the rank of the header
 * @param classAttribute the name of the class of this h element
 * @param idAttribute the name of the id of this h element 
 * @return returns an h element with a rank and text
 */
function createHElement(text, rank, classAttribute, idAttribute) {
  const hElement = document.createElement('h' + rank);
  hElement.setAttribute('class', classAttribute);
  hElement.setAttribute('id', idAttribute);  
  hElement.innerText = text;
  return hElement;
}

/** 
 * Creates an <img> element. 
 *
 * @param srcAttribute the link to the image
 * @param altAttribute the alternative text that will be displayed
 * @param classAttribute the name of the class of this img element
 * @param idAttribute the name of the id of this img element 
 * @return returns an img element
 */
function createImgElement(srcAttribute, altAttribute, classAttribute, 
    idAttribute) {
  const imgElement = document.createElement('img');
  imgElement.src = srcAttribute;
  imgElement.alt = altAttribute;
  imgElement.setAttribute('class', classAttribute);
  imgElement.setAttribute('id', idAttribute);
  return imgElement;
}

/** 
 * Creates a <p> element containing text. 
 *
 * @param text the text that will be displayed
 * @param classAttribute the name of the class of this p element
 * @param idAttribute the name of the id of this p element 
 * @return returns a p element with the text
 */
function createPElement(text, classAttribute, idAttribute) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  pElement.setAttribute('class', classAttribute);
  pElement.setAttribute('id', idAttribute);
  return pElement;
}

/** 
 * Creates an <a> element containing text. 
 *
 * @param text the text that will be displayed
 * @param targetAttribute the target attribute for this a element
 * @param hrefAttribute the href attribute for this a element
 * @param classAttribute the name of the class of this a element
 * @param idAttribute the name of the id of this a element 
 * @return returns an a element with the text
 */
function createAElement(text, targetAttribute, hrefAttribute, classAttribute, 
    idAttribute) {
  const aElement = document.createElement('p');
  aElement.innerText = text;
  aElement.setAttribute('target', targetAttribute);
  aElement.setAttribute('href', hrefAttribute);
  aElement.setAttribute('class', classAttribute);
  aElement.setAttribute('id', idAttribute);
  return aElement;
}

// miscellaneous functions

/** 
 * Toggles the display of an element. 
 * 
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

// Listing Page

/**
 * Create an element that shows a listing detailed view
 *
 * @param divCardContainerElement a div element where the listing will pop up
 * @param number the number assigned to this specific listing (the number 
 *     itself is insignificant)
 * @return a div with all the information pertaining to a listing
 */
function createListingDetailedView(divCardContainerElement, number) {

  const divCardElementId = 'listing' + number;
  const cardElementDisplay = "flex";
  const divCardElement = createDivElement(
      'toggleDisplay("'+ cardElementDisplay + '", "' + divCardElementId + '")', 
      'card shadow-box', divCardElementId);
  divCardContainerElement.appendChild(divCardElement);

  const divExitElement = createDivElement(
      '', 'exit', '');
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
      createAElement('Website Link', '_blank', '', 'listing-website', '')); 

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
