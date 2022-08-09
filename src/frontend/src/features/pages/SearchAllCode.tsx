import { SearchAllCodeText } from "../components/searchable/SearchAllCodeText";

export interface SearchAllCodeProps {}

export function SearchAllCode(props: SearchAllCodeProps) {
  //#// use Date().now to calculate timedelta and perform request after change every x seconds
  return (
    <div>
      <SearchAllCodeText />
    </div>
  );
}
