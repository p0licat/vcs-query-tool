import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
} from "@mui/material";
import FileContents from "./dto/intermediate/FileContents";

export interface CodeFileProps {
  fileName: string;
  contents: FileContents;
  // contentsPreviewStart
  // contentsPreviewEnd
  url: string;
}

export function CodeFile(props: CodeFileProps) {
  console.log(props.contents.contents);
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          {props.fileName}
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          {props.contents.contents}
        </Typography>
        <Typography variant="body2">{props.url}</Typography>
      </CardContent>
      <CardActions>
        <Button
          size="small"
          //onClick={() => props.redirectToSearchRepoContents(props.repoName)}
        >
          View contents
        </Button>
      </CardActions>
    </Card>
  );
}
