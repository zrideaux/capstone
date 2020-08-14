import { createButtonElement } from './htmlElement.js';

import { keyboardAccessibleOnClick } from './miscellaneous.js';

const goToTopButtonId = 'go-to-top';

export default function createScrollToTopButton(classAttribute = '', scrollTo) {
  const goToTopButton = createButtonElement('Go to top', classAttribute, 
      goToTopButtonId);
  keyboardAccessibleOnClick(goToTopButton, goToTop, goToTop);
  window.onscroll = function() {scrollFunction(scrollTo)};
  return goToTopButton;
}

// When the user clicks on the button, scroll to the top of the document
function goToTop() {
  document.body.scrollTop = 0; // For Safari
  document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

// When the user scrolls down 20px from the top of the document, show the button
// window.onscroll = function() {scrollFunction()};

function scrollFunction(scrollTo) {
  const goToTopButton = document.getElementById(goToTopButtonId);
  if (document.body.scrollTop > scrollTo || document.documentElement.scrollTop > scrollTo) {
    goToTopButton.style.display = "block";
  } else {
    goToTopButton.style.display = "none";
  }
}
