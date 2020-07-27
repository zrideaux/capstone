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
function createAElement(text, hrefAttribute, targetAttribute, classAttribute, 
    idAttribute) {
  const aElement = document.createElement('a');
  aElement.innerText = text;
  aElement.setAttribute("target", targetAttribute);
  aElement.setAttribute("href", hrefAttribute);
  aElement.setAttribute("class", classAttribute);
  aElement.setAttribute("id", idAttribute);
  return aElement;
}

/**
 * Creates a <br> element
 */
function createBrElement() {
  document.createElement("BR");
}

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
  divElement.setAttribute("onclick", onclickAttribute);
  divElement.setAttribute("class", classAttribute);
  divElement.setAttribute("id", idAttribute);
  return divElement;
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
  hElement.setAttribute("class", classAttribute);
  hElement.setAttribute("id", idAttribute);  
  hElement.innerText = text;
  return hElement;
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
  imgElement.setAttribute("class", classAttribute);
  imgElement.setAttribute("id", idAttribute);
  return imgElement;
}

/** 
 * Creates an <input> element of type checkbox containing text, a name and 
 *     value attribute as well as a class and id. 
 * 
 * @param text the text that will be displayed  
 * @param nameAttribute the name attribute for this input element
 * @param valueAttribute the value of this input
 * @param classAttribute the name of the class of this div element
 * @param idAttribute the name of the id of this div element
 * @return return an input element of type radio
 */
function createInputCheckboxElement(text, nameAttribute, valueAttribute, 
    classAttribute, idAttribute) {
  const inputElement = document.createElement('input');
  inputElement.setAttribute("type", "checkbox");
  inputElement.setAttribute("name", nameAttribute);
  inputElement.setAttribute("value", valueAttribute);
  inputElement.setAttribute("class", classAttribute);
  inputElement.setAttribute("id", idAttribute);
  inputElement.innerText = text;
  return inputElement;
}

/** 
 * Creates an <input> element of type radio containing text, a name and value 
 *     attribute as well as a class and id. 
 * 
 * @param text the text that will be displayed  
 * @param nameAttribute the name attribute for this input element
 * @param valueAttribute the value of this input
 * @param classAttribute the name of the class of this div element
 * @param idAttribute the name of the id of this div element
 * @return return an input element of type radio
 */
function createInputRadioElement(text, nameAttribute, valueAttribute, 
    classAttribute, idAttribute) {
  const inputElement = document.createElement('input');
  inputElement.setAttribute("type", "radio");
  inputElement.setAttribute("name", nameAttribute);
  inputElement.setAttribute("value", valueAttribute);
  inputElement.setAttribute("class", classAttribute);
  inputElement.setAttribute("id", idAttribute);
  inputElement.innerText = text;
  return inputElement;
}

/** 
 * Creates an <input> element of type text containing a name, minlength,   
 * maxlength, and placeholder attribute as well as a class and id. 
 * 
 * @param nameAttribute the name attribute for this input element
 * @param minLengthAttribute the minimum length of text accepted
 * @param maxLengthAttribute the maximum length of text accepted
 * @param placeholderAttribute the text that will be displayed before the user  *     inputs text
 * @param classAttribute the name of the class of this div element
 * @param idAttribute the name of the id of this div element
 * @return return an input element of type text
 */
function createInputTextElement(nameAttribute, minLengthAttribute,
    maxLengthAttribute, placeholderAttribute, classAttribute, idAttribute) {
  const inputElement = document.createElement('input');
  inputElement.setAttribute("type", "text");
  inputElement.setAttribute("name", nameAttribute);
  inputElement.setAttribute("minLength", minLengthAttribute);
  inputElement.setAttribute("maxLength", maxLengthAttribute);
  inputElement.setAttribute("placeholder", placeholderAttribute);
  inputElement.setAttribute("class", classAttribute);
  inputElement.setAttribute("id", idAttribute);
  return inputElement;
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
  pElement.setAttribute("class", classAttribute);
  pElement.setAttribute("id", idAttribute);
  return pElement;
}

/** 
 * Creates a <section> element containing a class and an id attribute. 
 *
 * @param classAttribute the name of the class of this section element
 * @param idAttribute the name of the id of this section element
 * @return returns a section element with a class, and an id
 */
function createSectionElement(classAttribute, idAttribute) {
  const sectionElement = document.createElement('section');
  sectionElement.setAttribute("class", classAttribute);
  sectionElement.setAttribute("id", idAttribute);
  return sectionElement;
}

/** 
 * Creates a <span> element containing text, class and id attribute. 
 *
 * @param text the text that will be displayed 
 * @param classAttribute the name of the class of this span element
 * @param idAttribute the name of the id of this span element
 * @return returns a span element with text, class, and id
 */
function createSpanElement(text, classAttribute, idAttribute) {
  const spanElement = document.createElement('span');
  spanElement.setAttribute("class", classAttribute);
  spanElement.setAttribute("id", idAttribute);
  spanElement.innerText = text;
  return spanElement;
}

export { 
  createAElement, 
  createBrElement,
  createDivElement, 
  createHElement, 
  createIElement, 
  createImgElement, 
  createInputCheckboxElement,
  createInputRadioElement,
  createInputTextElement, 
  createPElement, 
  createSectionElement, 
  createSpanElement
  };
