// Generated from grammars/OurScheme.g4 by ANTLR 4.13.2
// jshint ignore: start
import antlr4 from 'antlr4';
const serializedATN = [4,1,12,48,2,0,7,0,2,1,7,1,2,2,7,2,2,3,7,3,1,0,1,0,
1,0,3,0,12,8,0,1,0,1,0,1,0,3,0,17,8,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,
1,27,8,1,1,2,1,2,5,2,31,8,2,10,2,12,2,34,9,2,1,2,1,2,3,2,38,8,2,1,3,5,3,
41,8,3,10,3,12,3,44,9,3,1,3,1,3,1,3,0,0,4,0,2,4,6,0,0,55,0,16,1,0,0,0,2,
26,1,0,0,0,4,28,1,0,0,0,6,42,1,0,0,0,8,17,3,2,1,0,9,11,5,1,0,0,10,12,3,4,
2,0,11,10,1,0,0,0,11,12,1,0,0,0,12,13,1,0,0,0,13,17,5,2,0,0,14,15,5,4,0,
0,15,17,3,0,0,0,16,8,1,0,0,0,16,9,1,0,0,0,16,14,1,0,0,0,17,1,1,0,0,0,18,
27,5,10,0,0,19,27,5,5,0,0,20,27,5,6,0,0,21,27,5,7,0,0,22,27,5,8,0,0,23,27,
5,9,0,0,24,25,5,1,0,0,25,27,5,2,0,0,26,18,1,0,0,0,26,19,1,0,0,0,26,20,1,
0,0,0,26,21,1,0,0,0,26,22,1,0,0,0,26,23,1,0,0,0,26,24,1,0,0,0,27,3,1,0,0,
0,28,32,3,0,0,0,29,31,3,0,0,0,30,29,1,0,0,0,31,34,1,0,0,0,32,30,1,0,0,0,
32,33,1,0,0,0,33,37,1,0,0,0,34,32,1,0,0,0,35,36,5,3,0,0,36,38,3,0,0,0,37,
35,1,0,0,0,37,38,1,0,0,0,38,5,1,0,0,0,39,41,3,0,0,0,40,39,1,0,0,0,41,44,
1,0,0,0,42,40,1,0,0,0,42,43,1,0,0,0,43,45,1,0,0,0,44,42,1,0,0,0,45,46,5,
0,0,1,46,7,1,0,0,0,6,11,16,26,32,37,42];


const atn = new antlr4.atn.ATNDeserializer().deserialize(serializedATN);

const decisionsToDFA = atn.decisionToState.map( (ds, index) => new antlr4.dfa.DFA(ds, index) );

const sharedContextCache = new antlr4.atn.PredictionContextCache();

export default class OurSchemeParser extends antlr4.Parser {

    static grammarFileName = "OurScheme.g4";
    static literalNames = [ null, "'('", "')'", "'.'", "'''" ];
    static symbolicNames = [ null, "LEFT_PAREN", "RIGHT_PAREN", "DOT", "QUOTE", 
                             "INT", "FLOAT", "STRING", "NIL", "T", "SYMBOL", 
                             "WS", "COMMENT" ];
    static ruleNames = [ "sExp", "atom", "sExpList", "program" ];

    constructor(input) {
        super(input);
        this._interp = new antlr4.atn.ParserATNSimulator(this, atn, decisionsToDFA, sharedContextCache);
        this.ruleNames = OurSchemeParser.ruleNames;
        this.literalNames = OurSchemeParser.literalNames;
        this.symbolicNames = OurSchemeParser.symbolicNames;
    }



	sExp() {
	    let localctx = new SExpContext(this, this._ctx, this.state);
	    this.enterRule(localctx, 0, OurSchemeParser.RULE_sExp);
	    var _la = 0;
	    try {
	        this.state = 16;
	        this._errHandler.sync(this);
	        var la_ = this._interp.adaptivePredict(this._input,1,this._ctx);
	        switch(la_) {
	        case 1:
	            this.enterOuterAlt(localctx, 1);
	            this.state = 8;
	            this.atom();
	            break;

	        case 2:
	            this.enterOuterAlt(localctx, 2);
	            this.state = 9;
	            this.match(OurSchemeParser.LEFT_PAREN);
	            this.state = 11;
	            this._errHandler.sync(this);
	            _la = this._input.LA(1);
	            if((((_la) & ~0x1f) === 0 && ((1 << _la) & 2034) !== 0)) {
	                this.state = 10;
	                this.sExpList();
	            }

	            this.state = 13;
	            this.match(OurSchemeParser.RIGHT_PAREN);
	            break;

	        case 3:
	            this.enterOuterAlt(localctx, 3);
	            this.state = 14;
	            this.match(OurSchemeParser.QUOTE);
	            this.state = 15;
	            this.sExp();
	            break;

	        }
	    } catch (re) {
	    	if(re instanceof antlr4.error.RecognitionException) {
		        localctx.exception = re;
		        this._errHandler.reportError(this, re);
		        this._errHandler.recover(this, re);
		    } else {
		    	throw re;
		    }
	    } finally {
	        this.exitRule();
	    }
	    return localctx;
	}



	atom() {
	    let localctx = new AtomContext(this, this._ctx, this.state);
	    this.enterRule(localctx, 2, OurSchemeParser.RULE_atom);
	    try {
	        this.state = 26;
	        this._errHandler.sync(this);
	        switch(this._input.LA(1)) {
	        case 10:
	            this.enterOuterAlt(localctx, 1);
	            this.state = 18;
	            this.match(OurSchemeParser.SYMBOL);
	            break;
	        case 5:
	            this.enterOuterAlt(localctx, 2);
	            this.state = 19;
	            this.match(OurSchemeParser.INT);
	            break;
	        case 6:
	            this.enterOuterAlt(localctx, 3);
	            this.state = 20;
	            this.match(OurSchemeParser.FLOAT);
	            break;
	        case 7:
	            this.enterOuterAlt(localctx, 4);
	            this.state = 21;
	            this.match(OurSchemeParser.STRING);
	            break;
	        case 8:
	            this.enterOuterAlt(localctx, 5);
	            this.state = 22;
	            this.match(OurSchemeParser.NIL);
	            break;
	        case 9:
	            this.enterOuterAlt(localctx, 6);
	            this.state = 23;
	            this.match(OurSchemeParser.T);
	            break;
	        case 1:
	            this.enterOuterAlt(localctx, 7);
	            this.state = 24;
	            this.match(OurSchemeParser.LEFT_PAREN);
	            this.state = 25;
	            this.match(OurSchemeParser.RIGHT_PAREN);
	            break;
	        default:
	            throw new antlr4.error.NoViableAltException(this);
	        }
	    } catch (re) {
	    	if(re instanceof antlr4.error.RecognitionException) {
		        localctx.exception = re;
		        this._errHandler.reportError(this, re);
		        this._errHandler.recover(this, re);
		    } else {
		    	throw re;
		    }
	    } finally {
	        this.exitRule();
	    }
	    return localctx;
	}



	sExpList() {
	    let localctx = new SExpListContext(this, this._ctx, this.state);
	    this.enterRule(localctx, 4, OurSchemeParser.RULE_sExpList);
	    var _la = 0;
	    try {
	        this.enterOuterAlt(localctx, 1);
	        this.state = 28;
	        this.sExp();
	        this.state = 32;
	        this._errHandler.sync(this);
	        _la = this._input.LA(1);
	        while((((_la) & ~0x1f) === 0 && ((1 << _la) & 2034) !== 0)) {
	            this.state = 29;
	            this.sExp();
	            this.state = 34;
	            this._errHandler.sync(this);
	            _la = this._input.LA(1);
	        }
	        this.state = 37;
	        this._errHandler.sync(this);
	        _la = this._input.LA(1);
	        if(_la===3) {
	            this.state = 35;
	            this.match(OurSchemeParser.DOT);
	            this.state = 36;
	            this.sExp();
	        }

	    } catch (re) {
	    	if(re instanceof antlr4.error.RecognitionException) {
		        localctx.exception = re;
		        this._errHandler.reportError(this, re);
		        this._errHandler.recover(this, re);
		    } else {
		    	throw re;
		    }
	    } finally {
	        this.exitRule();
	    }
	    return localctx;
	}



	program() {
	    let localctx = new ProgramContext(this, this._ctx, this.state);
	    this.enterRule(localctx, 6, OurSchemeParser.RULE_program);
	    var _la = 0;
	    try {
	        this.enterOuterAlt(localctx, 1);
	        this.state = 42;
	        this._errHandler.sync(this);
	        _la = this._input.LA(1);
	        while((((_la) & ~0x1f) === 0 && ((1 << _la) & 2034) !== 0)) {
	            this.state = 39;
	            this.sExp();
	            this.state = 44;
	            this._errHandler.sync(this);
	            _la = this._input.LA(1);
	        }
	        this.state = 45;
	        this.match(OurSchemeParser.EOF);
	    } catch (re) {
	    	if(re instanceof antlr4.error.RecognitionException) {
		        localctx.exception = re;
		        this._errHandler.reportError(this, re);
		        this._errHandler.recover(this, re);
		    } else {
		    	throw re;
		    }
	    } finally {
	        this.exitRule();
	    }
	    return localctx;
	}


}

OurSchemeParser.EOF = antlr4.Token.EOF;
OurSchemeParser.LEFT_PAREN = 1;
OurSchemeParser.RIGHT_PAREN = 2;
OurSchemeParser.DOT = 3;
OurSchemeParser.QUOTE = 4;
OurSchemeParser.INT = 5;
OurSchemeParser.FLOAT = 6;
OurSchemeParser.STRING = 7;
OurSchemeParser.NIL = 8;
OurSchemeParser.T = 9;
OurSchemeParser.SYMBOL = 10;
OurSchemeParser.WS = 11;
OurSchemeParser.COMMENT = 12;

OurSchemeParser.RULE_sExp = 0;
OurSchemeParser.RULE_atom = 1;
OurSchemeParser.RULE_sExpList = 2;
OurSchemeParser.RULE_program = 3;

class SExpContext extends antlr4.ParserRuleContext {

    constructor(parser, parent, invokingState) {
        if(parent===undefined) {
            parent = null;
        }
        if(invokingState===undefined || invokingState===null) {
            invokingState = -1;
        }
        super(parent, invokingState);
        this.parser = parser;
        this.ruleIndex = OurSchemeParser.RULE_sExp;
    }

	atom() {
	    return this.getTypedRuleContext(AtomContext,0);
	};

	LEFT_PAREN() {
	    return this.getToken(OurSchemeParser.LEFT_PAREN, 0);
	};

	RIGHT_PAREN() {
	    return this.getToken(OurSchemeParser.RIGHT_PAREN, 0);
	};

	sExpList() {
	    return this.getTypedRuleContext(SExpListContext,0);
	};

	QUOTE() {
	    return this.getToken(OurSchemeParser.QUOTE, 0);
	};

	sExp() {
	    return this.getTypedRuleContext(SExpContext,0);
	};


}



class AtomContext extends antlr4.ParserRuleContext {

    constructor(parser, parent, invokingState) {
        if(parent===undefined) {
            parent = null;
        }
        if(invokingState===undefined || invokingState===null) {
            invokingState = -1;
        }
        super(parent, invokingState);
        this.parser = parser;
        this.ruleIndex = OurSchemeParser.RULE_atom;
    }

	SYMBOL() {
	    return this.getToken(OurSchemeParser.SYMBOL, 0);
	};

	INT() {
	    return this.getToken(OurSchemeParser.INT, 0);
	};

	FLOAT() {
	    return this.getToken(OurSchemeParser.FLOAT, 0);
	};

	STRING() {
	    return this.getToken(OurSchemeParser.STRING, 0);
	};

	NIL() {
	    return this.getToken(OurSchemeParser.NIL, 0);
	};

	T() {
	    return this.getToken(OurSchemeParser.T, 0);
	};

	LEFT_PAREN() {
	    return this.getToken(OurSchemeParser.LEFT_PAREN, 0);
	};

	RIGHT_PAREN() {
	    return this.getToken(OurSchemeParser.RIGHT_PAREN, 0);
	};


}



class SExpListContext extends antlr4.ParserRuleContext {

    constructor(parser, parent, invokingState) {
        if(parent===undefined) {
            parent = null;
        }
        if(invokingState===undefined || invokingState===null) {
            invokingState = -1;
        }
        super(parent, invokingState);
        this.parser = parser;
        this.ruleIndex = OurSchemeParser.RULE_sExpList;
    }

	sExp = function(i) {
	    if(i===undefined) {
	        i = null;
	    }
	    if(i===null) {
	        return this.getTypedRuleContexts(SExpContext);
	    } else {
	        return this.getTypedRuleContext(SExpContext,i);
	    }
	};

	DOT() {
	    return this.getToken(OurSchemeParser.DOT, 0);
	};


}



class ProgramContext extends antlr4.ParserRuleContext {

    constructor(parser, parent, invokingState) {
        if(parent===undefined) {
            parent = null;
        }
        if(invokingState===undefined || invokingState===null) {
            invokingState = -1;
        }
        super(parent, invokingState);
        this.parser = parser;
        this.ruleIndex = OurSchemeParser.RULE_program;
    }

	EOF() {
	    return this.getToken(OurSchemeParser.EOF, 0);
	};

	sExp = function(i) {
	    if(i===undefined) {
	        i = null;
	    }
	    if(i===null) {
	        return this.getTypedRuleContexts(SExpContext);
	    } else {
	        return this.getTypedRuleContext(SExpContext,i);
	    }
	};


}




OurSchemeParser.SExpContext = SExpContext; 
OurSchemeParser.AtomContext = AtomContext; 
OurSchemeParser.SExpListContext = SExpListContext; 
OurSchemeParser.ProgramContext = ProgramContext; 
