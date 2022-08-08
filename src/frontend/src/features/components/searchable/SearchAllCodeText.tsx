import { TextField } from "@mui/material";

export interface SearchAllCodeTextProps {}

export function SearchAllCodeText(props: SearchAllCodeTextProps) {
  const handleTextChange = (event: any) => {
    let newValue = event.target.value;
    store.dispatch(updateCodeSearchText({ newText: newValue }));
  };

  return (
    <div>
      <TextField onChange={handleTextChange} />
    </div>
  );
}
