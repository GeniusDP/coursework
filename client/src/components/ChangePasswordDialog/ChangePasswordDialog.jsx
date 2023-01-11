import { Container } from '@material-ui/core';
import { useInput } from '@mui/base';
import React from 'react';
import CustomAppBar from '../CustomAppBar/CustomAppBar';
import CustomFormInput from '../CustomFormInput.jsx/CustomFormInput';

const ChangePasswordDialog = () => {
  const newPassword = useInput("", {isEmpty: true, minLength: 5, maxLength: 50});
  const repeatNewPassword = useInput("", {isEmpty: true, minLength: 5, maxLength: 50});
  const currentPassword = useInput("", {isEmpty: true, minLength: 5, maxLength: 50});
  
  return (
    <>
      <CustomAppBar/>
      <Container>
      </Container>
    </>
  );
};

export default ChangePasswordDialog;