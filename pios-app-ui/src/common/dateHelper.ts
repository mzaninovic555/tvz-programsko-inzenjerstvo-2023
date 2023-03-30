export const formatDate = (date?: Date, includeHours = true) => {
  if (!date) {
    return 'N/A';
  }
  date = new Date(date);
  const d = `${date.getFullYear()}-${normalize(date.getMonth() + 1)}-${normalize(date.getDate())}`;
  const t = `${normalize(date.getHours())}:${normalize(date.getMinutes())}`;
  return `${d}${includeHours ? ' ' + t : ''}`;
};

export const normalize = (num: number): string => {
  return num.toLocaleString('de', {minimumIntegerDigits: 2});
};
