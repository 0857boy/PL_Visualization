let currentResizer,
  currentSection,
  nextSection,
  originalSectionWidth,
  originalSectionHeight,
  originalMouseX,
  originalMouseY;

window.onload = () => {
  document.querySelectorAll(".resizer").forEach((resizer) => {
    resizer.addEventListener("mousedown", mousedown);
  });

  var ace = require('ace-builds/src-noconflict/ace');
  var oop = ace.require("ace/lib/oop");
  var TextMode = ace.require("ace/mode/text").Mode;
  var TextHighlightRules = ace.require("ace/mode/text_highlight_rules").TextHighlightRules;

  var CustomHighlightRules = function () {
    var keywordMapper = this.createKeywordMapper({
      "keyword": "define"  // 將 "define" 這個關鍵字變成黃色
    }, "text", true);

    this.$rules = {
      "start": [{
        token: keywordMapper,
        regex: "\\b\\w+\\b",
        caseInsensitive: true
      }]
    };
  };

  oop.inherits(CustomHighlightRules, TextHighlightRules);

  var Mode = function () {
    this.HighlightRules = CustomHighlightRules;
  };

  oop.inherits(Mode, TextMode);

  exports.Mode = Mode;

  ace.define('ace/theme/my_theme', ['require', 'exports', 'module', 'ace/lib/dom'], function (require, exports, module) {
    exports.isDark = false;
    exports.cssClass = 'ace-my-theme';
    exports.cssText = '.ace-my-theme .ace_keyword {color: yellow;}';  // 將你的關鍵字設為黃色
    var dom = require('../lib/dom');
    dom.importCssString(exports.cssText, exports.cssClass);
  });

  var input = ace.edit("editor");
  input.setTheme("ace/theme/my_theme");
  input.getSession().setMode(new Mode());

  let output = ace.edit("editor");
  input.setTheme("ace/theme/my_theme");
  input.getSession().setMode(new Mode());

};

function mousedown(e) {
  currentResizer = e.target;
  currentSection = currentResizer.previousElementSibling;
  nextSection = currentResizer.nextElementSibling;

  if (currentResizer.classList.contains("horizontal-resizer")) {
    originalSectionWidth = currentSection.getBoundingClientRect().width;
    originalMouseX = e.pageX;
  } else if (currentResizer.classList.contains("vertical-resizer")) {
    originalSectionHeight = currentSection.getBoundingClientRect().height;
    originalMouseY = e.pageY;
  }

  document.addEventListener("mousemove", mousemove);
  document.addEventListener("mouseup", mouseup);
}

function mousemove(e) {
  if (currentResizer.classList.contains("horizontal-resizer")) {
    const dx = e.pageX - originalMouseX;
    const newCurrentSectionWidth =
      ((originalSectionWidth + dx) * 100) /
      document.getElementById("main").offsetWidth;
    currentSection.style.flex = `0 0 ${newCurrentSectionWidth}%`;
  } else if (currentResizer.classList.contains("vertical-resizer")) {
    const dy = e.pageY - originalMouseY;
    const newCurrentSectionHeight =
      ((originalSectionHeight + dy) * 100) /
      document.getElementById("main").offsetHeight;
    currentSection.style.flex = `0 0 ${newCurrentSectionHeight}%`;
  }
}

function mouseup() {
  document.removeEventListener("mousemove", mousemove);
  document.removeEventListener("mouseup", mouseup);
}

$(".toggle-icon").click(function () {
  var subSection = $(this).parent().next();

  var isCurrentlyVisible = subSection.css("flex-basis") !== "0%";

  if (isCurrentlyVisible) {
    subSection.css("flex-basis", "0%");
    $(this).removeClass("fa-chevron-up").addClass("fa-chevron-down");
  } else {
    subSection.css("flex-basis", "40%");
    $(this).removeClass("fa-chevron-down").addClass("fa-chevron-up");
  }
});
