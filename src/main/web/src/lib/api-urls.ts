const KB = "p=>b, b~>f, b~>w, p~>!f";
const QUERY = "p~>f";
const GET_KB_URL = "/api/formulas";
const GET_QUERY_URL = "/api/query";
const VALIDATE_KB_URL = "/api/validate/formulas";
const VALIDATE_QUERY_URL = "/api/validate/query";
const VALIDATE_DEFAULT_KB_URL = `${VALIDATE_KB_URL}/${KB}`;
const VALIDATE_DEFAULT_QUERY_URL = `${VALIDATE_QUERY_URL}/${QUERY}`;

export {
  GET_KB_URL,
  GET_QUERY_URL,
  VALIDATE_KB_URL,
  VALIDATE_QUERY_URL,
  VALIDATE_DEFAULT_KB_URL,
  VALIDATE_DEFAULT_QUERY_URL,
};
