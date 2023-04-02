import React from 'react';
import {Button} from 'primereact/button';

interface ErrorProps {
  children?: JSX.Element | JSX.Element[];
}

interface ErrorState {
  error?: string | Error;
}

class ErrorBoundary extends React.Component<ErrorProps, ErrorState> {
  state: ErrorState = {};

  static getDerivedStateFromError(error: Error | unknown) {
    console.error(error);
    // Update state so the next render will show the fallback UI.
    return {error: error};
  }

  copyError() {
    if (!this.state.error) {
      return;
    }
    const e = this.state.error;
    navigator.clipboard.writeText(e instanceof Error ? `${e.message}\n${e.stack}` : e).catch(console.warn);
    alert('Error info copied to clipboard');
  }

  render() {
    if (!this.state.error) {
      return this.props.children;
    }

    const error = (<div style={{color: 'red'}}>
      <h3 className="mb-2">
        {this.state.error instanceof Error ? this.state.error?.message : this.state.error.toString()}
      </h3>
      {this.state.error instanceof Error && <p style={{fontFamily: 'monospace', whiteSpace: 'pre', fontSize: 'xx-small'}}>
        {this.state.error.stack}
      </p>}
      <Button label="Copy error info" className="boundary-btn p-button-danger" icon="pi pi-copy" onClick={() => this.copyError()}/>
    </div>);

    return (<div style={{height: '100vh'}} className="w-full flex justify-content-center align-items-center">
      <h2>{error}</h2>
    </div>);
  }
}

export default ErrorBoundary;
