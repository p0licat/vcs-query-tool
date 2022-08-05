import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
} from "@mui/material";

export interface RepoCardProps {
  repoName: string;
  description: string;
  url: string;
  redirectToSearchRepoContents: Function;
}

export function RepoCard(props: RepoCardProps) {
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          {props.repoName}
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          {props.description}
        </Typography>
        <Typography variant="body2">{props.url}</Typography>
      </CardContent>
      <CardActions>
        <Button
          size="small"
          onClick={() => props.redirectToSearchRepoContents(props.repoName)}
        >
          View contents
        </Button>
      </CardActions>
    </Card>
  );
}
