import { createButtonElement } from './htmlElement.js';

import { keyboardAccessible, keyboardAccessibleOnClick } from './miscellaneous.js';

const goToTopButtonId = 'back-to-top';

// Add JQuery to file
var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

/**
 * Create a scroll to top button which appears when a user scrolls past the 
 *     scrollPast parameter.
 * 
 * @param classAttribute The name of the class of this button element.
 * @param scrollPast A number in px from the top of the document.
 * @return a button that scrolls to the top of the page.
 */
function createScrollToTopButton(classAttribute = '', scrollPast) {
  const goToTopButton = createButtonElement('Back to top', classAttribute + 
      ' card-button', goToTopButtonId);
  goToTopButton.style.display = 'none';
  makeElementScrollToTop(goToTopButton);
  window.onscroll = function() {scrollFunction(goToTopButton, scrollPast)};
  return goToTopButton;
}

// Scrolls to the top of the page when called with smooth scroll.
function scrollToTop() {
  $(document).ready(function(){
    $('html, body').animate({
          scrollTop: 0
        });
  });
}

/**
 * When the user scrolls past the scrollPast parameter (number in px) from the 
 *     top of the documents, it will show the element.
 *
 * @param element An element whose display will toggle on and off.
 * @param scrollPast A number in px from the top of the document.
 */
function scrollFunction(element, scrollPast) {
  if (document.body.scrollTop > scrollPast || document.documentElement.scrollTop > scrollPast) {
    element.style.display = "block";
  } else {
    element.style.display = "none";
  }
}

/**
 * Add a Back to top button to an elementContainer that appears when the user 
 *     scrolls past the a scrollPastElement.
 *
 * @param buttonClassAttribute The name of the class of this button element.
 * @param elementContainerId The id of the element to add this button to.
 * @param scrollPastElementId The id of the element that the user will scroll 
 *     past in order to make the button appear.
 */
function addBackToTopButton(buttonClassAttribute, elementContainerId, 
    scrollPastElementId) {
  const elementContainer = document.getElementById(elementContainerId);
  const positionFromTop = document.getElementById(scrollPastElementId)
      .getBoundingClientRect().top;

  elementContainer.appendChild(
      createScrollToTopButton(buttonClassAttribute, positionFromTop));
}


/**
 * Add a Back to top button before a beforeElement that appears when the user 
 *     scrolls past the a scrollPastElement.
 *
 * @param buttonClassAttribute The name of the class of this button element.
 * @param beforeElementId The id of the element to add this button before.
 * @param scrollPastElementId The id of the element that the user will scroll 
 *     past in order to make the button appear.
 */
function addBackToTopButtonBefore(buttonClassAttribute, beforeElementId, 
    scrollPastElementId) {
  const beforeElement = document.getElementById(beforeElementId);
  const parentDiv = beforeElement.parentNode;
  const positionFromTop = document.getElementById(scrollPastElementId)
      .getBoundingClientRect().top;

  parentDiv.insertBefore(
      createScrollToTopButton(buttonClassAttribute, positionFromTop),
      beforeElement);
}

/**
 * Makes an element scroll to the top of the page when clicked/entered.
 *
 * @param element An element that will scroll to the top of the page when 
 *     clicked/entered.
 * @param tabindex The tabindex of this element.
 */
function makeElementScrollToTop(element, tabindex = '0') {
  element.style.cursor = 'pointer';
  keyboardAccessible(element, scrollToTop, scrollToTop, tabindex);
}

/**
 * Make the nav bar scroll to the top when clicked/entered.
 * A nav bar in this project has an id of 'nav-box'.
 * This is a feature mostly created for mobile devices.
 */
function makeNavBarScrollToTop() {
  makeElementScrollToTop(document.getElementById('nav-box'), '-1');
}

export {
  addBackToTopButton,
  addBackToTopButtonBefore,
  createScrollToTopButton,
  makeElementScrollToTop,
  makeNavBarScrollToTop,
  scrollFunction,
  scrollToTop
}
